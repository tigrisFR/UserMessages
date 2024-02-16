# UserMessages Design and Development Notes

### Addendum 02/14: Notes on Navigation and Screens

*I had not mentioned much about the navigation part of my code so far. Since it may be unclear for developers unfamiliar with Google's jetpack Compose, here's some more notes to help make sense of it:*

Same as the rest of the code I used the latest guidelines from Google: the compose and navigation-compose libs.
The app has a unique android Activity and no Fragments. It relies on Composable screens that are called by a NavController when it's time to be displayed.
This navigation controller manages a stack of NavBackStackEntries (where each entry corresponds to a destination, in the order they were navigated to, with the active one at the top).
A navigation graph is declared and built dynamically via a NavHost: it ties up a tree of composable destinations, composable screens and navigation callbacks, and allows for type-safe navigation arguments to be passed along.

As a result, the app starts on the AllMessages screen, and from there:
- we can either go to the message Composer screen by tapping the FloatingActionButton
- or tap a user name tag to go to a UserMessages screen for that user

The Composer screen is a dead-end. It is "popped" from the navigation stack either by "back action/gesture" or when the state of its viewmodel signals that the message was sent. It accepts a user name as a type-safe navigation parameter which, if present, prefills the author field.

From the UserMessages screen we can:
- "back action/gesture" to the previous entry in the stack -the AllMessages screen is the only possibility right now-
- or go to the Composer screen via the FAB, in which case the user name is passed as a navigation parameter to prefill the author field there.

Final note: as for the rest of the code, it has been carefully encapsulated to support modularisation: we can easily move the screen, the navigation and the viewmodel files to a separate gradle module for each one of the screens/features.
Presently, it's a bit overkill for 3 fairly simple screens but in the long run:
- this greatly helps to reduce the amount of time developpers spend recompiling classes or running whatever unit-tests or lint checks are hooked up to their pre-commit hook for instance
- as well as limits the number of revision conflicts between team members
- and enforces clear and clean architecure/dependencies
### end of Addendum

## TESTING
- All 3 use-cases work
- The GET use-cases will require you to click fetch before displaying results: indeed the ViewModels don't fetch upon initialization. This is so that I could correctly debug the post and get requests in succession. It would be very simple to put a fetch call in the init blocks in each of these ViewModels though.
- I focused on instrumented tests as they helped me debugging various issues with the remote API and rule out where it came from (took me a VERY long time to figure out that the body JSON objects are actually stringified and I ended up making custom deserializer for these.

**Run instrumented test from Android studio or gradle wrapper from the command line to easily post Messages to the endpoint.**
```./gradlew connectedAndroidTest```
They will appear in your ```/app/build/reports/androidTests/connected/``` folder from the project root directory.

Future directions would focus on building unit tests and more instrumented tests e.g. UI Compose instrumented tests via https://developer.android.com/jetpack/compose/testing-cheatsheet 
-> Then hooking it up to gitHub actions to run automatically
-> for each push you could also require to run some static checks like Lint and Unit tests in the modules impacted

I blew past the alloted time but I had a lot of fun.

Cheers !

-------------------
# *** NOTES BELOW ***

The lack of message unique identifier makes it hard for storing messages locally and sync with remote.
-> Note that we could hack a UUID and insert/extract it from the message message JSON field

**Essentially we are going to focus on implementing 3 use-cases (one for each remote Api call).**

The UI needs to provide a screen for each use case/ each api call
- a **AllMessages** screen: displays all messages grouped by users and can be refreshed to get new ones.
- a **UserMessages** screen: same as above but displays only the ones sent by that user
- a **Message Composer** screen: allows to create a Message from scratch
	whatever user name is used is stored to SharedPref and prepopulates the field next time
- a HomeScreen that allows to navigate to all these separate screens

**Now, we could merge the Home and AllMessages screens together while navigating to UserMessages by clicking on user names and to
Message Composer screen by clicking a floating button**


*Remark: It seems the user-specific GET is a bit unecessary if there is no pagination for the general one:
	if there was pagination, navigating to a user-specific Feed could give us all that user's messages
	while saving a lot of time and API calls (as that user may otherwise appear way down in the general response pages).
	But here if we indeed are getting all of the data at once, we don't really need it.*

  Nevertheless we can still provide a separate UserMessages screen that uses the user-specific api call


## ENTITIES
  The GET endpoints only expose one: a ```UserMessages``` entity.
  While we may hope to have a more granular User AND Message entities setup where User has a 1 to many relationship with Message,
  and Message holds the sender user's name, we have to make do.
  
  ```UserMessages``` contain a name: we will make the **assumption that it's unique** and serves as a user unique identifier.
	

## MODELS
It's tempting to not even bother making a Model for User since all the data we need for our use-cases can be stored in a MessageModel:

  ### ```MessageModel```
		- subject: from "subject" JSON field
	 	- text: from "message" JSON field
	 	- author: from "user" JSON field *as a bonus perhaps*

**But let's drill down:**
#### ```cons:```
- we'd lose the straightforward conversion from JSON. Aligning with remote API calls is often a good practice to avoid a convoluted architecture and obscure client-specific business logic that slows down feature development
- if when showing all messages, we really want to only show them grouped by author: then flattening them into a giant list of MessageModel without a use-case to justify it causes 2 hits in performance (when flattening from JSON and again when grouping them by User for UI) 
#### ```pros:```
- handling a flat list of message would streamline storing to db for offline use
- handling nested list can easily cause UI performance issues (as the LazyList or RecyclerView cannot properly predict the size of the nested list)
from a UI standpoint it would actually be beneficial to flatten the data in a list and group its element by username to then show them in a single list.

**-> It's especially that last point about UI performance that makes me favor the flattening approach as taking a performance hit on data fetching operation is less of a problem.**

Uncertainty about API:
- not sure we're garanteed to have only one occurence of a user value per response call
- messages may be appearing in chronological order
**-> Both these would hint at using the "flattening" approach**

### ```UserMessageModel``` *perhaps not needed*
		- username: from "user" JSON field
		- List<MessageModel>
  
## Brief Architecture overview
Since the main source of friction seems to be around the inflexibility of UserMessages entities, "UI->Domain<-Data" architecture seems appropriate:
- we can isolate the cost of that friction (e.g.: flattening the List of UserMessage entity into MessageModel) to UseCase objects in the Domain layer and thus streamline the implementation of our Data layer
- We should make a clear separation between our 3 layers with the dependency flowing from the Domain layer outward.
- In each layer we'll separate our classes according to the 3 usecases except for the Apis at the edges:
	- Application, Activity, Navigation, and Compose Apis, as well as whatever our Dependency Graph for DI is
	- Local Store and Network layers implementations likely Retrofit and Proto Store or Room


Bonus:
- Manual dependency injection or hilt 
- Proto Data Store for persisting - since we don't have a need for partial update here this could be a quick and dirty way to get disk persistence
- Play with LazyColumn headers for User name item *DONE*
	- perhaps stickyheaders ones *DONE*
	- make them collapsible either via simple conditional or fancy AnimatedVisibility

- Make the HomeScreen/AllMessagesScreen also allow displaying messages by subject, or in order *ALMOST DONE*
- perhaps a selection bar allows for typing a user name or selecting "All users" *WON'T DO*

- extract and cache users from all messages and populate suggestions for author field in the Message composer
- insert/extract timestamp in the JSON "message" field in order to display in chronological order in Messages feeds

- store drafts locally
- insert/extract UUID via the message message field to allow keying our ListItems and thus allowing sync between remote, db and UI lists
- DB local data store for offline access (better with unique identifier for messages otherwise we need to wipe everything with every fetch)
- paging support (requires unique identifier for messages)
- highlight unseen messages


