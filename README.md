# android_toolbox
A set of classes designed to make android app development easier. 


## Fragment based navigation
No need to argue about creating a Fragment or an Activity anymore. Fragments are for displaying UI, Activities for navigation from one fragment to another. Have you activity override PushPop Activity and enjoy method such as Push(Fragment f), Pop(), PopToRoot(), PresentModally(Fragment f)... All the Heavy lifting is done for you.
Better yet, use a messenger based approach (with the help of the great eventBus : https://github.com/greenrobot/EventBus) to send navigation event for a code cleaner than ever. And you even have the back button working properly

```java

public class MainActivity extends PushPopActivity {
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void askForPermissionsIfNeeded() {
        // we don't need anything here
    }

    @Override
    public Class<? extends Fragment> getStartActivityClass() {
        return CityListFragment.class;
    }

    @Override
    protected boolean prepareSideMenu(Menu menu) {
        return false; // no side menu... yet...
    }

    @Override
    protected void onSideMenuItemSelected(MenuItem element) {
        // ignore
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SegueEvents event) {
        switch (event)
        {
            // When a city is selected, present the detail of said city (push animation)
            case CitySelected:
                push(new CityDetailFragments(), event.getBundle());
                break;
        }
    };
}
```

You can even create a drawer toggle just by providing implementaion to the prepareSideMenu() method.

## Lists
Tired of writing tiresome boilerplate code for just displaying a simple list of item... That stuff should be easy and straighforward right. Got a list of item of type T, a layout with some widget, you just want to bind some properties of the class T to a widget... Well, no you can, have a look, here is how we bind a list of string to a listview in a fragment (note the use of the generic class) 

```java

public class CityListFragment extends SmartListFragment<String> { // we specify the type of element to display

    /* this is hjust the title of the screen */
    @Override
    public String getTitle() {
        return "Cities";
    }
    
   /* here is our datasource, we should return the list of element to display, get them from wherever you want in the order you want them to be displayed */
    @Override
    public List<String> getElements() {
        SmartList<String> strs = new SmartList<>(new String[]{"Rouen", "Paris", "Chicago", "Budapest", "Buccarest", "Prague", "Tokio","Pekin", "Moscow", "St Petersbourg", "Kiev", "Bratislava", "Rome", "Belgrade", "Barcelone", "Los Angeles", "San Fransisco", "Las Vegas", "Tijuana", "Lisbone", "Porto", "Venise", "Lubljana", "Sarajevo", "Bruxelles", "Liege", "Amsterdam"});
        strs.sort(new SortDescripor<String>() {
            @Override
            public boolean isBefore(String item1, String item2) {
                return item1.compareTo(item2) < 0;
            }
        });
        return strs;
    }
    
    /* Now that we have the list of elements, lets get the layout, here we take the android default with ne textView but feel free to use your own */
    @Override
    public int getCellLayout() {
        return android.R.layout.simple_list_item_1;
    }
    
    /* the layout will be inflated several time, here you can register the widget you care for, those that will be updated depending on the item to be displayed */
   @Override
    public void bindViews(View cell, ViewHolder vh) {
        vh.registerView(TEXT_VIEW_FIELD, cell.findViewById(android.R.id.text1));
    }
    
    /* Now do the binding */
    @Override
    public void bindViewHolder(ViewHolder vh, String item) {
        ((TextView)(vh.getView(TEXT_VIEW_FIELD))).setText(item);
    }
    
    // Want the pull to refresh experience? Tell us what to do then and call completion.succeeds(true) when done (more about promise later)
    @Override
    protected void refreshDataSet(Promise<Boolean> completion) {
        completion.succeeds(true);
    }

   /* What happen when a item get clicked? Well here we are sending an event with eventbus that will be catch by our activity */
    @Override
    public void onSelect(String element) {
        EventBus.getDefault().post(SegueEvents.CitySelected.addBundle(BundleBuilder
                .withString("CitySelected", element)
                .createBundle())
        );
    }
   
   private static final String TEXT_VIEW_FIELD = "TEXT_VIEW_FIELD";

}
```

## forms
If navigation and list were not enought, here is a tool to make great form fragments. When ovverriding formFragment, you have 2 methods to implements configureForm() will let you specify the form fields, onCellDataChanged() will let you know when the user changes a value. As far as cell goes, you can implements your own, but some are already here :
CheckableCell for a yes/no value
DatePickerCell
EditableTextCell
ListPickerCell for choosing an item of type T from a list represented as a list of strings
NumberPickerCell
TimePickerCell...
ActivityItemCell for choosing from a list of "action" represented as a list of image + text and presented in a grid
And so much ready to override for your own needs...
Look how easy it is to create a field that would let the user select a location (LatLng) from a map:


```java
private class CoordinatePickerCell extends PickerDialogCell<LatLng> implements GoogleMap.OnCameraMoveListener, View.OnClickListener {

        private GoogleMap googleMap;
        private LatLng value;
        private Dialog dialog;

        public CoordinatePickerCell(int tag, FormFragment form, String label, LatLng coord) {
            super(tag, form, label, coord);
        }

        @Override
        protected Dialog onCreateDialog(Context c, LatLng value) {
            final Context context = c;
            final LatLng coord = value;
            final CoordinatePickerCell lis = this;
            
            // prepare a dialog
            Dialog dialog = new Dialog(c);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.map_dialog);
           
           // display the map and center it on the coord location
            MapView mv = (MapView) dialog.findViewById(R.id.mapView);
            mv.onCreate(new Bundle());
            mv.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    lis.googleMap = googleMap;
                    MapsInitializer.initialize(context);
                    int zoom = 7;
                    googleMap.addMarker(new MarkerOptions().position(coord));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, zoom));
                    googleMap.setOnCameraMoveListener(lis);
                }
            });
            mv.onResume();
            
            // register as a click listener for the ok button
            Button bt = (Button) dialog.findViewById(R.id.buttonSet);
            bt.setOnClickListener(this);
            this.dialog = dialog;

            return dialog;
        }

        // this is how the LatLng will be displaued when the map is not visible, fell free to customize
        @Override
        protected String onUpdatingValueText(LatLng value) {
            return value.toString();
        }

       // We will store locally the center coordinate as the user moves the map
        @Override
        public void onCameraMove() {
            if(this.googleMap != null) this.value = googleMap.getCameraPosition().target;
        }

        // When click on button OK, dismmiss the dialog and update the value notifying the form of the change
        @Override
        public void onClick(View view) {
            if(dialog != null) dialog.dismiss();
            setValue(value, true);
        }
    }
```
So with that done, we just need to present our form

```java
    @Override
    public void configureForm() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.city = (City) bundle.getSerializable("CitySelected");
        }

        addRow(new TextViewCell(NameField, this , "Name", this.city.getName()));


        insertSectionHeader("Coordinate");
        addRow(new CoordinatePickerCell(CoordField, this , "Coordinate", new LatLng(city.getLatitude(), city.getLongitude())));

    }
```

and we can react to changes

```java
    @Override
    public void onCellDataChanged(Cell cell, Object newValue) {
        switch (cell.getTag())
        {
            case CoordField:
                LatLng latLng = (LatLng) newValue;
                city.setLongitude(latLng.longitude);
                city.setLatitude(latLng.latitude);
                break;
        }

        // Maybe save here into persistent store
    }
```

Well we are not even close to see what we can do with those... readonly fields, hidding cells depending on the value of other, etc...

## SQLite
Remember when you had to write "CREATE TABLE CITY(blablabla..);" and spend some valuable time (yours!) trying to figure out where is the bug? whitepace or coma missing? Well, that's it! we are over.
Let's create a table that will store Cities (name, longitude, latitude)
1st, create a POJO 

```java
public class City extends SQLiteEntity {

    private String name;
    private double longitude;
    private double latitude;

    public City(String name)
    {
        this.name = name;
    }

    public City(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }
}
```
Notice that you have to override SQLiteEntity

2nd step : lets create a repository that will be our main singleton class to do CRUD operation
We will add a SQLiteHelper field that we will init in the constructor (you will need a Context object)
```java
public class CitiesRepository  {

   // the SQLite Helper class
    private SQLiteHelper helper;
    
    // the city table, we will need ref to that later
    private SQLiteTable<City> tableCity;

    public CitiesRepository(Context ctx) {
    
        // let's create the Cities db file with version 1
        this.helper = new SQLiteHelper(ctx, "Cities", null, 1);
        
        // Now, the table city! Remember SQL? I don't
        this.tableCity = new SQLiteTable("City", new SQLiteTableSerializer<City>(){

           // How do you get from content value to City?
            @Override
            public City deserialize(ContentValues item) {

                City c = new City(item.getAsString("name"));
                c.setLatitude(item.getAsDouble("latitude"));
                c.setLongitude(item.getAsDouble("longitude"));
                return c;
            }

            // How do you get from City to ContentValue?
            @Override
            public ContentValues serialize(City object) {
                ContentValues cv = new ContentValues();
                cv.put("name", object.getName());
                cv.put("longitude", object.getLongitude());
                cv.put("latitude", object.getLatitude());
                return cv;
            }
 
            // 'CREATE TABLE ' + tbName + '(' ... LOL
            @Override
            public void onCreateTable(SQLiteTable<City> table) {
                table.addColumn("name", SQLiteTable.TypeTEXT);
                table.addColumn("longitude", SQLiteTable.TypeREAL);
                table.addColumn("latitude", SQLiteTable.TypeREAL);
            }
        });
        // add the table to the DB
        this.helper.addTable(this.tableCity);
    }
}
```

That's it! Seen any SQL? You're welcome!

Now to the query...
What about a fetch request. No SQLite here, just use the SQLQuery class
You got it all select, where clause, orderby, group by... Here is a sample that will show you how to combine where clauses and perform an oredered select.
```java
    /// Return all the city from the db, order alphabetically where long/lat are different from 0
    public ArrayList<City> getAllCities() {

        SQLiteQuery.Predicate p1 = SQLiteQuery.Predicate.notEqual("longitude", 0); // longitude == 0
        SQLiteQuery.Predicate p2 = SQLiteQuery.Predicate.notEqual("latitude", 0); // latitude == 0
        SQLiteQuery.Predicate p3 = SQLiteQuery.Predicate.and(p1, p2); // latitude == 0 && longitude == 0;
        SQLiteQuery.Predicate p4 = SQLiteQuery.Predicate.not(p3); // NOT (latitude == 0 && longitude == 0);


        return this.helper.executeQuery(SQLiteQuery.selectFrom(this.tableCity)
                .where(p4)
                .orderBy("name"));
    }
```

What about inserting object in db... please :)
```java
    public void AddCity(String name, double longitude, double latitude) {
        this.helper.createEntity(this.tableCity, new City(name, longitude, latitude));
    }
```

you can also use updateEntity(SQLiteTable<U> table, U item) and deleteEntity(SQLiteTable<U> table, U item) to well... you know...
Check out the SQLiteQueryfor more about predicates and distinct query.
  
## promise
That's how it all started. I needed a better way than async task to chain async code and get callbacks.
A promise is a completion marker to a task.
Let's say that you have a method  `void doSomethingVEryLong(ICallback<Integer> callback)` when the method finishes, the callack will be call with either and error or the result. Should you want to wait for it to return to start another task, you end up with a 40 store indentation code...
Now you can change your methode to, instead of returning nothing and taking a callback as param, to return a Promise
(Please refer to PromiseTest.java for more of what you can do )

```java
    public <T> Promise<T> doSomethingVeryLong()
    {
        final Promise<T> p = new Promise<T>();

        doSomethingVEryLong(new ICallback<T>() {
            @Override
            public void OnFinish(T result, Exception error) {
                if(error != null)
                {
                    p.fails(error);
                }
                else
                {
                    p.succeeds(result);
                }
            }
        });
        return p;
    }
```  

Now you can write code like :

```java
        Promise<Integer> p = doSomethingVeryLong();

        p.addSuccesHandler(new Handler<Integer>() {
            @Override
            public void onFinished(Integer result) {
                // this code will be called as soon as the task is done with success.
                // If this handler is added after completion, the code will be called instantly
            }
        });

        p.addErrorHandler(new Handler<Exception>() {
            @Override
            public void onFinished(Exception result) {
                // this code will be called as soon as the task fails.
                // If this handler is added after completion, the code will be called instantly
            }
        });
```  
Ok, I see it, you might wonder what it changes if you need to chain task... well let's say that you want to run the process 3 time, one after the other... 
see how you would write it with callbacks?

```java
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
``` 
See how you have to write things in the opposite order of how they will happens?

Look with promise

``` java
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
``` 
Code is written and reads as it will occur. The promise variable is overriden each time with the latest action.
And if you want to for loop... then it's easy

```java
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
```
## Service Locator

Want to keep just a single object of a given class alive and availble from evrywhere?
Singleton just make you throw up?
ACID much but DI fells like a bit overkill?
Go ServiceLocator

```java
// An interface (must extend IService, but it's just a marker no worry)
public interface ICityRepository extends ServiceLocator.IService
{
    ArrayList<City> getAllCities();

    void AddCity(String name, double longitude, double latitude);

    void Update(City city);
}

// A class that implement this interface, with a constructor like we dont like.
public class CitiesRepository implements ICityRepository {
public CitiesRepository(Context ctx) {...}
 ...
}

// then just, once an for all in your Application class do this
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CitiesRepository repo = new CitiesRepository(this);
        ServiceLocator.put(ICityRepository.class, repo);
    }
}

// and wherever you want do that
public class CityListFragment extends SmartListFragment<City> {
   
    @Override
    public List<City> getElements() {
        return ServiceLocator.get(ICityRepository.class).getAllCities();
    }

   ...
}

```

## more to explore
Explore, ask away... there are good things! Contributors welcome
