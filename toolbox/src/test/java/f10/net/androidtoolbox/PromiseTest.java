package f10.net.androidtoolbox;

import android.os.AsyncTask;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import com.google.common.primitives.Ints;

import net.jodah.concurrentunit.Waiter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import f10.net.androidtoolbox.asynclib.Handler;
import f10.net.androidtoolbox.asynclib.Promise;
import f10.net.androidtoolbox.asynclib.PromiseAsyncTask;
import f10.net.androidtoolbox.asynclib.Task;
import f10.net.androidtoolbox.collections.SmartList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by fl0 on 15/04/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PromiseTest {


    /**
     * Let's create a simple async task that will do a kinda long job, like figuring out if a number is prime or not.
     */

    private static class PrimeCheckAsyncTask extends AsyncTask<Integer, Void, Boolean>
    {

        @Override
        protected Boolean doInBackground(Integer... params) {
            return isPrime(params[0]);
        }

        boolean isPrime(int n) {
            //check if n is a multiple of 2
            if (n%2==0) return false;
            //if not, then just check the odds
            for(int i=3;i*i<=n;i+=2) {
                if(n%i==0)
                    return false;
            }
            return true;
        }
    }

    /**
     * Let's test this async task with a prime and a not prime number in a very simple way
     */

    @Test
    public void test_prime_succeed() throws Exception {

        final Waiter waiter = new Waiter();

        PrimeCheckAsyncTask check = new PrimeCheckAsyncTask(){
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                assertTrue(aBoolean);
                waiter.resume();
            }
        };

        check.execute(89); // 89 is a prime.

        waiter.await();

    }

    @Test
    public void test_prime_failed() throws Exception {

        final Waiter waiter = new Waiter();

        PrimeCheckAsyncTask check = new PrimeCheckAsyncTask(){
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                assertFalse(aBoolean);
                waiter.resume();
            }
        };

        check.execute(90); // 90 is NOT a prime.

        waiter.await();

    }

    /**
     * Now that it works, let see write a class with an async method that uses the async task with the listener pattern
     */

    public static class PrimeCheckerWithListener{

        public interface Listener{
            void onResult(boolean isPrime);
        }

        static void check(int number, final Listener listener)
        {
            PrimeCheckAsyncTask check = new PrimeCheckAsyncTask(){
                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    listener.onResult(aBoolean);
                }
            };

            check.execute(number);
        }
    }

    /**
     * Let's test if it works with the simple case frome before
     */


    @Test
    public void test_listener_simple_succeed() throws Exception {

        final Waiter waiter = new Waiter();

        PrimeCheckerWithListener.check(89, new PrimeCheckerWithListener.Listener() {
            @Override
            public void onResult(boolean isPrime) {
                assertTrue(isPrime);
                waiter.resume();
            }
        });

        waiter.await();

    }

    @Test
    public void test_listener_simple_failed() throws Exception {

        final Waiter waiter = new Waiter();

        PrimeCheckerWithListener.check(90, new PrimeCheckerWithListener.Listener() {
            @Override
            public void onResult(boolean isPrime) {
                assertFalse(isPrime);
                waiter.resume();
            }
        });

        waiter.await();

    }

    /**
     * Now, let's seee how we can, using the listener patterns deal with " uses case :
     * - I want to run 3 times the async process, with three different numbers, ONE AFTER THE OTHER.
     * - I want to run N time (for loop) the the async process, with N different numbers, ONE AFTER THE OTHER.
     * - I want to run N time (for loop) the the async process, no need to wait for one task to start the other, and be called when all is done.
     */


    public static int[] NUMBERS_TO_CHECK = new int[]{67, 90, 89}; // 67 and 89 are primes, 90 isn't.

    @Test
    public void test_listener_chain() throws Exception {

        final Waiter waiter = new Waiter();

        final Set<Integer> primes = new HashSet<Integer>();

        final PrimeCheckerWithListener.Listener onThirdNumberListener = new PrimeCheckerWithListener.Listener() {
            @Override
            public void onResult(boolean isPrime) {
                if(isPrime) primes.add(NUMBERS_TO_CHECK[2]);

                assertTrue(primes.size() == 2);
                assertTrue(primes.contains(NUMBERS_TO_CHECK[0]));
                assertTrue(primes.contains(NUMBERS_TO_CHECK[2]));

                waiter.resume();
            }
        };

        final PrimeCheckerWithListener.Listener onSecondNumberListener = new PrimeCheckerWithListener.Listener() {
            @Override
            public void onResult(boolean isPrime) {
                if(isPrime) primes.add(NUMBERS_TO_CHECK[1]);
                PrimeCheckerWithListener.check(NUMBERS_TO_CHECK[2], onThirdNumberListener);
            }
        };


        PrimeCheckerWithListener.Listener onFirstNumberListener = new PrimeCheckerWithListener.Listener() {
            @Override
            public void onResult(boolean isPrime) {
                if(isPrime) primes.add(NUMBERS_TO_CHECK[0]);
                PrimeCheckerWithListener.check(NUMBERS_TO_CHECK[1], onSecondNumberListener);
            }
        };

        PrimeCheckerWithListener.check(NUMBERS_TO_CHECK[0], onFirstNumberListener);

        waiter.await();

    }

    @Test
    public void test_listener_chain_loop() throws Exception {

        Set<Integer> set = ContiguousSet.create(Range.closed(1, 100), DiscreteDomain.integers());
        int[] first100Numbers = Ints.toArray(set);

        final Set<Integer> primes = new HashSet<Integer>();

        for(final Integer i : first100Numbers)
        {
            final Waiter wait = new Waiter();

            PrimeCheckerWithListener.check(i, new PrimeCheckerWithListener.Listener() {
                @Override
                public void onResult(boolean isPrime) {
                    if(isPrime)  primes.add(i);
                    wait.resume();
                }
            });

            wait.await();

        }

        assertTrue(primes.size() == 25); // there is 25 primes in the first 100 numbers;


    }

    @Test
    public void test_listener_unchained() throws Exception {

        final Waiter wait = new Waiter();

        final Set<Integer> set = ContiguousSet.create(Range.closed(1, 100), DiscreteDomain.integers());
        int[] first100Numbers = Ints.toArray(set);

        final Set<Integer> primes = new HashSet<Integer>();

        final Set<Integer> testedNumber = new HashSet<Integer>();

        for(final Integer i : first100Numbers)
        {

            PrimeCheckerWithListener.check(i, new PrimeCheckerWithListener.Listener() {
                @Override
                public void onResult(boolean isPrime) {
                    if(isPrime)  primes.add(i);
                    testedNumber.add(i);
                    if(testedNumber.size() == set.size())
                    {
                        assertTrue(primes.size() == 25);
                        wait.resume();
                    }
                }
            });

        }

        wait.await();

    }

    /**
     * Problemes :
     * Test 1 : the code is written from bottom to top, step 1 is at the bottom of the method while step 3 is a t the top.
     * Test 2 : it is really hard to wait for one task to be done without using an external tool.
     * Test 3 : Need to include a variable (lock) that we test every time to see if we are done yet.
     */


    /**
     * Now, let's write a class with an async method that uses the async task with the promise pattern
     */

    public static class PrimeCheckerWithPromise{

        static Promise<Boolean> check(int number) // instead of taking a listener, the method returns a promise.
        {
            final Promise<Boolean> promise = new Promise<Boolean>();

            PrimeCheckAsyncTask check = new PrimeCheckAsyncTask(){
                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    promise.succeeds(aBoolean); // when the task is done, the promise is terminated either with succeeds(result) or fail(error)
                }
            };

            check.execute(number);

            return promise;
        }
    }

    /**
     * let's do the simple test again to see if it works;
     */

    @Test
    public void test_promise_simple_succeed() throws Exception {

        final Waiter waiter = new Waiter();

        Promise<Boolean> p = PrimeCheckerWithPromise.check(89);
        p.addSuccesHandler(new Handler<Boolean>() {
            @Override
            public void onFinished(Boolean result) {
                assertTrue(result);
                waiter.resume();
            }
        });

        waiter.await();

    }

    @Test
    public void test_promise_simple_failed() throws Exception {

        final Waiter waiter = new Waiter();

        PrimeCheckerWithPromise.check(90).addSuccesHandler(new Handler<Boolean>() {
            @Override
            public void onFinished(Boolean result) {
                assertFalse(result);
                waiter.resume();
            }
        });

        waiter.await();

    }

    /**
     * Now, let's re-explore or 3 scenarii with the promise pattern
     */


    @Test
    public void test_promise_chain() throws Exception {

        final Waiter waiter = new Waiter();

        final Set<Integer> primes = new HashSet<Integer>();

        Promise<Boolean> promise = PrimeCheckerWithPromise.check(NUMBERS_TO_CHECK[0]);


        promise.addSuccesHandler(new Handler<Boolean>() {
            @Override
            public void onFinished(Boolean result) {
                if(result) primes.add(NUMBERS_TO_CHECK[0]);
            }
        });

        promise = PromiseAsyncTask.chain(promise, new Task<Boolean>() {

            @Override
            public void perform(Promise<Boolean> completionPromise) {
                PrimeCheckerWithPromise.check(NUMBERS_TO_CHECK[1]).link(completionPromise);
            }

        });

        promise.addSuccesHandler(new Handler<Boolean>() {
            @Override
            public void onFinished(Boolean result) {
                if(result) primes.add(NUMBERS_TO_CHECK[1]);
            }
        });

        promise = PromiseAsyncTask.chain(promise, new Task<Boolean>() {

            @Override
            public void perform(Promise<Boolean> completionPromise) {
                PrimeCheckerWithPromise.check(NUMBERS_TO_CHECK[2]).link(completionPromise);
            }

        });

        promise.addSuccesHandler(new Handler<Boolean>() {
            @Override
            public void onFinished(Boolean result) {
                if(result) primes.add(NUMBERS_TO_CHECK[2]);
            }
        });

        PromiseAsyncTask.chain(promise, new Task<Boolean>() {

            @Override
            public void perform(Promise<Boolean> completionPromise) {

                assertTrue(primes.size() == 2);
                assertTrue(primes.contains(NUMBERS_TO_CHECK[0]));
                assertTrue(primes.contains(NUMBERS_TO_CHECK[2]));

                waiter.resume();

                completionPromise.succeeds(true);
            }

        });


        waiter.await();

    }

    @Test
    public void test_promise_chain_loop() throws Exception {

        final Waiter waiter = new Waiter();

        // find out how many numbers are primes in 1 - 9999
        final Set<Integer> primes = new HashSet<Integer>();
        Promise<Boolean> promise = Promise.fromResult(false); // first instant promise
        for(int i= 1 ; i < 1000; i++)
        {

            // will start evaluating newt value of i as soon as the previous is finished
            final int finalI = i;
            promise = PromiseAsyncTask.chain(promise, new Task<Boolean>() {
                @Override
                public void perform(Promise<Boolean> completionPromise) {
                    PrimeCheckerWithPromise.check(finalI).link(completionPromise);
                }
            });

            // if the evaluation is successfull, add the number to the list of primes
            promise.addSuccesHandler(new Handler<Boolean>() {
                @Override
                public void onFinished(Boolean result) {
                    if(result) primes.add(finalI);
                }
            });
        }
        promise.addSuccesHandler(new Handler<Boolean>() {
            @Override
            public void onFinished(Boolean result) {
                // explores primes
                assertTrue(primes.size() == 25); // there is 25 primes in the first 100 numbers;
                waiter.resume();
            }
        });

        waiter.await();

    }

    @Test
    public void test_promise_unchained() throws Exception {

        final Waiter wait = new Waiter();

        final Set<Integer> set = ContiguousSet.create(Range.closed(1, 100), DiscreteDomain.integers());
        int[] first100Numbers = Ints.toArray(set);

        final Set<Integer> primes = new HashSet<Integer>();

        Promise<Void> whenAll = Promise.fromResult(null);

        for(final Integer i : first100Numbers)
        {

            Promise<Boolean> promise = PrimeCheckerWithPromise.check(i);
            promise.addSuccesHandler(new Handler<Boolean>() {
                @Override
                public void onFinished(Boolean result) {
                    if(result) primes.add(i);
                }
            });
            whenAll = Promise.whenAll(new Promise[]{whenAll, promise});
        }

        whenAll.addSuccesHandler(new Handler<Void>() {
            @Override
            public void onFinished(Void result) {
                assertTrue(primes.size() == 25);
                wait.resume();
            }
        });

        wait.await();

    }

    /**
     * Conclusions :
     * Test 1 : the code is step by step in the reading direction. Task are chained one after the other. What to do with the result, and what to do next are handled separtly. If one step fails, the error cascade till the end of the chain.
     * Test 2 : The PromiseAsyncTask.chain(promise, task) guaranty that the task will be executed only after AND if the previous promise succeed
     * Test 3 : the Promise.whenAll(promises) methods is a great way to get a promise that will succeeds once all the given promises succeeds. If one fails, the whenAll promise fails too. To ignore failures, use whenAll(promises, true), that will then return a promise that will succed when every promise have terminated (either by succeeds or fails)
     */


    /**
     * some other cool stuff
     */

    /**
     * dealing with error is easy, promise can be set to succeed or fail. The proper handlers are then called.
     * @throws Exception
     */
    @Test
    public void test_failure() throws Exception {

        final Waiter wait = new Waiter();

        final Promise<Integer> promise = new Promise<Integer>();

        new PrimeCheckAsyncTask(){
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                promise.fails(new Exception("I failed"));
            }
        }.execute(543);

        promise.addSuccesHandler(new Handler<Integer>() {
            @Override
            public void onFinished(Integer result) {
                wait.fail("should not be called");
            }
        });

        promise.addErrorHandler(new Handler<Exception>() {
            @Override
            public void onFinished(Exception exception) {
                assertTrue(exception.getMessage().equals("I failed"));
                wait.resume();
            }
        });

        wait.await();

    }


    /**
     * Sometimes you need to wait a little (or a long time) before executing some code.
     * Like dismmissing an ad after a (always-to-long-but-that-what-your-boss-want) while.
     * Here is a way to get a promise that will succeed only after the given delay.
     * You can then add a success handler or chain a task to it.
     * @throws Exception
     */
    @Test
    public void test_do_something_in_the_future() throws Exception {

        final Waiter wait = new Waiter();

        final int expectedDelay = 10000; // 10 seconds.

        final Date start = new Date();

        Promise<Date> delay = Promise.fromResultAfterDelay(start, expectedDelay);

        // will be exectuted after delay, you can also use PromiseAsyncTask.chain() to execute a more complex task in background after delay
        delay.addSuccesHandler(new Handler<Date>() {
            @Override
            public void onFinished(Date result) {

                Date end = new Date();

                long actualDelay = end.getTime() - result.getTime();

                Assert.assertEquals(expectedDelay, actualDelay, 1000);

                wait.resume();

            }
        });

        wait.await(2 * expectedDelay);

    }

    /**
     * As a convenience method to perform a background task in the future.
     * @throws Exception
     */
    @Test
    public void test_do_something_in_the_future_2() throws Exception {

        final Waiter wait = new Waiter();

        final int expectedDelay = 10000; // 10 seconds.

        final Date start = new Date();

        Promise<Long> delay = PromiseAsyncTask.afterDelay(new Task<Long>() {
            @Override
            public void perform(Promise<Long> promise) {

                Date end = new Date();

                long actualDelay = end.getTime() - start.getTime();

                promise.succeeds(actualDelay);

            }
        }, expectedDelay);

        delay.addSuccesHandler(new Handler<Long>() {
            @Override
            public void onFinished(Long actualDelay) {

                assertTrue(expectedDelay < actualDelay);

                wait.resume();

            }
        });

        wait.await(20000);
    }


    /**
     * test the PromiseAsyncTask.setTimeOut(promise, delay) feature that allow you to start a timer that will fails() the promise after the given time.
     * @throws Exception
     */
    @Test
    public void test_timeout() throws Exception {

        final Waiter wait = new Waiter();

        Promise<Boolean> promise = new Promise<Boolean>();

        promise.setFailedAfterDelay(1000); // fails after 1 seconde

        promise.addSuccesHandler(new Handler<Boolean>() {
            @Override
            public void onFinished(Boolean result) {
                wait.fail("Promise should not succeed");
            }
        });

        promise.addErrorHandler(new Handler<Exception>() {
            @Override
            public void onFinished(Exception result) {
                wait.resume();
            }
        });

        wait.await(10000); // wait 10 sec max

    }



    /**
     * This test creates a chain of Tasks that will test 5 known primes numbers. After the third test, we include a task that fails.
     * We then test that the primes set contains only the 3 first numbers (test for the 2 last numbers that happens after the voluntary failure should not be executed)
     * and that the error is cascading to the last promise so that an error that happens upstream can be catch at the last step
     * @throws Exception
     */
    @Test
    public void test_cascade_error() throws Exception{

        final Waiter wait = new Waiter();

        final Set<Integer> primes = new HashSet<Integer>();

        Promise<Boolean> promise = PromiseAsyncTask.chain(Promise.fromResult(true), new Task<Boolean>() {
            @Override
            public void perform(Promise<Boolean> completionPromise) {
                final int test = 11;
                Promise<Boolean> p = PrimeCheckerWithPromise.check(test);
                p.addSuccesHandler(new Handler<Boolean>() {
                    @Override
                    public void onFinished(Boolean result) {
                        if(result) primes.add(test);
                    }
                });
                p.link(completionPromise);
            }
        });

        promise = PromiseAsyncTask.chain(promise, new Task<Boolean>() {
            @Override
            public void perform(Promise<Boolean> completionPromise) {
                final int test = 17;
                Promise<Boolean> p = PrimeCheckerWithPromise.check(test);
                p.addSuccesHandler(new Handler<Boolean>() {
                    @Override
                    public void onFinished(Boolean result) {
                        if(result) primes.add(test);
                    }
                });
                p.link(completionPromise);
            }
        });

        promise = PromiseAsyncTask.chain(promise, new Task<Boolean>() {
            @Override
            public void perform(Promise<Boolean> completionPromise) {
                final int test = 37;
                Promise<Boolean> p = PrimeCheckerWithPromise.check(test);
                p.addSuccesHandler(new Handler<Boolean>() {
                    @Override
                    public void onFinished(Boolean result) {
                        if(result) primes.add(test);
                    }
                });
                p.link(completionPromise);
            }
        });

        promise = PromiseAsyncTask.chain(promise, new Task<Boolean>() {
            @Override
            public void perform(Promise<Boolean> completionPromise) {
                completionPromise.fails(new Exception("voluntary exception"));
            }
        });

        promise = PromiseAsyncTask.chain(promise, new Task<Boolean>() {
            @Override
            public void perform(Promise<Boolean> completionPromise) {
                final int test = 73;
                Promise<Boolean> p = PrimeCheckerWithPromise.check(test);
                p.addSuccesHandler(new Handler<Boolean>() {
                    @Override
                    public void onFinished(Boolean result) {
                        if(result) primes.add(test);
                    }
                });
                p.link(completionPromise);
            }
        });

        promise = PromiseAsyncTask.chain(promise, new Task<Boolean>() {
            @Override
            public void perform(Promise<Boolean> completionPromise) {
                final int test = 89;
                Promise<Boolean> p = PrimeCheckerWithPromise.check(test);
                p.addSuccesHandler(new Handler<Boolean>() {
                    @Override
                    public void onFinished(Boolean result) {
                        if(result) primes.add(test);
                    }
                });
                p.link(completionPromise);
            }
        });

        promise.addSuccesHandler(new Handler<Boolean>() {
            @Override
            public void onFinished(Boolean result) {
                wait.fail("Promise should not succeed");
            }
        });

        promise.addErrorHandler(new Handler<Exception>() {
            @Override
            public void onFinished(Exception exception) {
                assertTrue(primes.size() == 3);
                assertTrue(exception.getMessage().equals("voluntary exception"));
                wait.resume();
            }
        });

        wait.await();
    }

    /**
     * A promise already succeeded...
     * @throws Exception
     */
    @Test
    public void test_succeededPromise() throws Exception {
        final Waiter wait = new Waiter();

        Promise<Integer> s = Promise.fromResult(42);

        s.addSuccesHandler(new Handler<Integer>() {
            @Override
            public void onFinished(Integer result) {
                assertTrue(result == 42);
                wait.resume();
            }
        });

        wait.await();
    }

    @Test
    public void test_linked_succeed() throws Exception {

        final Waiter wait = new Waiter();

        Promise<Integer> initial = Promise.fromResult(42);

        Promise<Integer> test = new Promise<Integer>();

        initial.link(test); // forwards succeeds(result) or fails(exception) calls.

        test.addSuccesHandler(new Handler<Integer>() {
            @Override
            public void onFinished(Integer result) {
                assertTrue(result == 42);
                wait.resume();
            }
        });

        wait.await();

        /*
         Notice that handlers can be added even if the promises is already terminated.
         In this case, $initial is already terminated (succeeds with result = 42) when we call the link() method
         The result is yet passed to $test when the link is establish.
         We then add the success handler to $test and, since it is already terminated, the handler is called immediately.
         There is no need now to worry about registering to late to async task.
          */

    }

    /**
     * let say that we have a task, such as opening the connection to a db, that MUST be done once and only once, but be check before each SQL calls.
     * instead of having a if(!db_is_open) { open() } in each sql call methods, we can leverage the promise patterns.
     * Here we will test 3 numbers to see if they are prime (spoiler: they are!), and if so, store them in a Set
     * Unfortunatly for us, the set that we start with is not empty and contains numbers from 1 to 100. Therefore, it need to be cleared first.
     * We assume here, that clearing the set takes times and is done asynchronously (linke a connection to a db)
     * We also assume that computation can start / finish before clearing the set is done
     * When a primeCheck is finished it needs to store the number into the set, but it doesn't know if the set has previously be cleared or not.
     * Overclearing it might result in loosing primes that have been store by other operation, not clearing it might result in wrong values stored in the set.
     * @throws Exception
     */
    @Test
    public void test_delay_result_treatment_after_something_async() throws Exception {

        final Waiter wait = new Waiter();

        final Set<Integer> primes = ContiguousSet.create(Range.closed(1, 100), DiscreteDomain.integers()); // dirty collection

        final int[] values = new int[]{17, 37, 71};

        // compute
        final Promise<Boolean> test1 = PrimeCheckerWithPromise.check(values[0]);
        final Promise<Boolean> test2 = PrimeCheckerWithPromise.check(values[1]);
        final Promise<Boolean> test3 = PrimeCheckerWithPromise.check(values[2]);

        /*
         The PromiseAsyncTask.from create a promise from a Task<T> object.
         The task starts immediatly.
         The returned promised is linked to the completionPromise received by the task, so that the call to "completionPromise.succeeds(Boolean.TRUE);" succeeds initializeSet with the result Boolean.TRUE as well.
         */

        Promise<Boolean> initializeSet = PromiseAsyncTask.afterDelay(new Task<Boolean>() {
            @Override
            public void perform(final Promise<Boolean> completionPromise) {

                primes.clear();
                completionPromise.succeeds(true);

            }
        }, 3000);

        // also we dont know in wich order p1, p2 and p3 will finish, we know that they will start and probably finish BEFORE initializeSet is done
        // but we can delay the treatment of the result until after initializeSet is done

        initializeSet.addSuccesHandler(new Handler<Boolean>() {
            @Override
            public void onFinished(Boolean result) {
                test1.addSuccesHandler(new Handler<Boolean>() {
                    @Override
                    public void onFinished(Boolean result) {
                        if(result) primes.add(values[0]);
                    }
                });
                test2.addSuccesHandler(new Handler<Boolean>() {
                    @Override
                    public void onFinished(Boolean result) {
                        if(result) primes.add(values[1]);
                    }
                });
                test3.addSuccesHandler(new Handler<Boolean>() {
                    @Override
                    public void onFinished(Boolean result) {
                        if(result) primes.add(values[2]);
                    }
                });
            }
        });


        Promise.whenAll(new Promise[]{test1, test2, test3, initializeSet}).addSuccesHandler(new Handler<Void>() {
            // let's wait for everybody to be done before doing our checks.
            @Override
            public void onFinished(Void result) {
                assertTrue(primes.size() == 3);
                wait.resume();
            }
        });

        wait.await(60000);

    }





}
