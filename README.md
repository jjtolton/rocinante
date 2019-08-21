# Rocinante

### tl;dr
A self-documenting, data-driven, event-sourced, reactive datascript SPA template that lets you start coding immediately. 

> "What giants?" said Sancho Panza....<br>
> "It is easy to see," replied Don Quixote, "that thou art not used to this business of adventures; those are giants; and if thou art afraid, away with thee out of this and betake thyself to prayer while I engage them in fierce and unequal combat."<br>
> So saying, he gave the spur to his steed Rocinante, heedless of the cries his squire Sancho sent after him, warning him that most certainly they were windmills and not giants he was going to attack.<br>
> -- [Cervantes](http://www.online-literature.com/cervantes/don_quixote/12/)



# Quickstart

## Installation

To run the demo:
```bash
# paste me into your terminal
bash <(cat << eof
clj -Sdeps '{:deps {seancorfield/clj-new {:mvn/version "0.5.5"}}}' -m clj-new.create https://github.com/HiImJayHireMe/rocinante@3813da2d5eb548266da81a85615d011d06566de5 some.example
cd some.example
make
make build
xdg-open resources/public/index.html || open resources/public/index.html
eof
)
```

General usage:
```bash
clj -Sdeps '{:deps {seancorfield/clj-new {:mvn/version "0.5.5"}}}' -m clj-new.create https://github.com/HiImJayHireMe/rocinante@3813da2d5eb548266da81a85615d011d06566de5 project.name
cd project.name
make
```

To recompile `webpack` bundle:
`yarn webpack`

To use `figwheel-main`:
1. `bash bridge.sh`
2. Connect to `.nrepl-port` from your favorite IDE
3. Evaluate the contents of `figwheel.clj` in your REPL environment.


## Requirements

- Mac or Linux OS
- `Java`
- `Clojure`
- `rlwrap`
- `npm`
- `yarn`

# Motivation

Every ClojureScript tutorial ever: 

![How to Draw an Owl](images/2019/08/owl.jpg)

Everyone who I introduce to ClojureScript has been immediately turned off by the tooling.  The myriad of build options and relative complexity of each tool is overwhelming for the newcomer and an annoying boilerplate chore for the seasoned professional.  You shouldn't have to go through hours of setup requiring in-depth knowledge of tooling and meticulous attention to detail just for a `hello-world`.  

The difficulty of mastering the tooling confounds the issue of how to create a scalable web application.  Developers will often do whatever it takes to get the application working, leaving a path of tech debt behind them.  Aside from providing a quick setup for a ClojureScript development environment, this [clj-new](https://github.com/seancorfield/clj-new) template serves as a guide and foundation for maintaining, scaling, and testing a large and complex SPA -- and hopefully bring back some of the FUN of ClojureScript!

# Features

## This is

- a batteries-included, data-driven, event-sourced, CQRS, reagent-based SPA template featuring a working TODO app
- skewed towards libraries over frameworks
- `nREPL`/`figwheel-main`-ready out of the box to use with your favorite IDE
- prepared with a curated `:deps` selection balancing minimalism and exploratory development
- an extensible folder structure emphasizing modularity, separation of concerns, and maintainability (heavily based on the [re-frame](https://github.com/Day8/re-frame) SPA philosophy)
- equipped with reactive [datascript](https://github.com/tonsky/datascript) functions (much like [posh](https://github.com/mpdairy/posh) and [re-posh](https://github.com/denistakeda/re-posh))
- `yarn`/`webpack` [workflow](https://figwheel.org/docs/npm.html) ready to go
- advanced-compilation-friendly
- preloaded with [@material-ui](https://material-ui.com/), [antd](https://ant.design/), and [bootstrap](https://getbootstrap.com/) `npm` modules so you can start prototyping rapidly.
- [WebWorker API](https://developer.mozilla.org/en-US/docs/Web/API/Web_Workers_API) ready
- Websocket ready
- A solid client-side foundation for [Web After Tomorrow](https://tonsky.me/blog/the-web-after-tomorrow/)

## This isn't

- production ready (see below for how to fix that)
- concise -- it's meant to be extensible and maintainable with minimal "magic"

# How it works

User and procedural actions create `events`.

```clojure
;; an event
{:event :some/event
 :data  "some data"}
```

how this might pop up:

```clojure
(defn yo-button []
  [:button {:on-click
    #(events/event! {:event :user/yo!!
                     :data  "Yo!!"})}
    "Yo!!"])
```

Events go in the `events-channel`, where they are picked up by the `event-loop`.

They are put in the `events-log` and then mapped to `handlers`.

```clojure
(ns some.lib.events.user)

(defn event! [e]
  {:notify [{:event :notify/success
             :data  (:data e)}]})
```

This mapping returned by `event!` is a `handler-map`. It specifies one or more `handlers` that are keyed to the event `:user/yo!!`.

The idea is to keep your `events` _side-effect-free_ and _specific_ to business logic and your `handlers` _generic_ to application side-effects. You can reuse multiple `handlers` to create new `events`.

```clojure

;; (ns some.views.main)
(defn yo-button []
  (let [clicks (rx/user-clicks)] ;; more on `rx` in a minute
    (fn []
      [:button {:on-click
        #(events/event!
          {:event :user/yo!!
           :data  {:text "Yo!!"
                   :clicks (inc @clicks)}})}
          (str "Yo clicks:" @clicks)])))
```

(**Note**: the `ns` below only responds to one `event`, effectively ignoring the `:event` key -- we'll get to it later)

```clojure
;; (ns some.lib.events.user)
(defn event! [e]
  (let [clicks (get-in e [:data :clicks] 0)
        text   (get-in e [:data :text])]]
        {:notify [{:event :notify/success
                   :data  text}]
         :data   [{:event :data/transact
                   :data  [{:db/ident         :user/clicks
                            :user.clicks/count clicks}]}]}))
```

Here we are invoking the `:notify` and `:data` handler modules with the events `:notify/success` and `:data/transact`, respectively.

The `handlers` are invoked asyncronously in `go` blocks and might look like this:

(**Note**: again, in this `handler` namespace, the `:event` key is ignored -- we'll get to it later)

```clojure
(ns some.lib.handlers.data
  (:require [some.lib.data.ds :as ds]))

(defn handle! [e]
  (ds/transact! (:data e)))
```

```clojure

(ns some.lib.handlers.notify
  (:require [antd]))

(defn notify! [e]
  (js/antd.message.success (str (:data e))))
```

Now our datascript database has been updated and the user has been notified.

## The Five Giants

`Rocinante` is about having complete control over 
five major web application operations.  These tasks are:

- Events
- Handlers
- Background processes
- Data subscriptions
- Data manipulation functions

### Events/Handlers

We've covered these briefly.  In general,
you don't want `on-click`/`on-change` handlers to cause
side effects -- you want them to generate `events`
that go on an `event` queue, are converted to `handler` maps,
and then get handled by `handlers`.  

### Background processes

Background processes are `go-loops` or `go` blocks
that periodically generate `events` without user 
interaction.  Just like user `events`, I would
caution against having them cause side-effects directly.

### Data subscriptions

`Rocinante` comes with powerful reactive `datascript` tools
for managing application state.  (learn more about datalog syntax [here](http://www.learndatalogtoday.org/))

```clojure

(defn user-clicks [user-name]
  (ds/q-rx '[:find ?clicks .
             :in $ ?user 
             :where 
             [[:user/name ?user] :user/clicks ?clicks]]
             user-name))
```

Here, `user-clicks` returns a `reagent` _reaction_, not just static data.


### Data operations

`Rocinante` comes with convenient ways to update state
via the `ds/transact!` method.  

```clojure 
(defn increment-user-clicks! [user-name clicks]
  (ds/transact! [{:user/name   user-name 
                  :user/clicks (inc clicks)}]))
```


These data operations should only be called by handlers (for the sake of your sanity).



## Opinionated Detail

`Rocinante` does not employ component local state. This is not an accident. `Rocinante` implores you to test from the `REPL`. Testing component local state from the `REPL` is annoying.

```clojure

(defn component-local-state []
  (let [clicks (reagent/atom 0)]
     [:button
       {:on-click #(swap! clicks inc)}
       (str "Clicks:" @clicks)]))

(defn subscribed-component-state []
  (let [clicks (rx/user-clicks)]
    [:button
      {:on-click events/increment-clicks}
      (str "Clicks:" @clicks)]))
```

While the difference may seem subtle, `component-local-state` is self-contained. `subscribed-component-state` involves up to sixteen additional namespaces:

- `some.events` (main API for `events`)
- `some.lib.events` (`event` module dispatching)
- `some.lib.events.user` (domain specific `events`)
- `some.procs` (for the `event-loop`, main entry point for background processes)
- `some.lib.procs` (`proc` module dispatching, if required)
- `some.lib.procs.user` (domain specific `procedures`)
- `some.handlers` (main API for `handlers`)
- `some.lib.handlers` (`handler` module dispatching)
- `some.lib.handlers.user` (domain specific `handlers`)
- `some.data` (main API for `data` processes)
- `some.lib.data` (module dispatching layer for `data`, if necessary)
- `some.lib.data.user` (domain specific `data` operations) 
- `some.lib.data.ds` (for reactive datascript)
- `some.reactions` (main API for data `reactions`)
- `some.lib.reactions` (module dispatch layer for `reactions`)
- `some.lib.reactions.user` (domain specific `reactions`)

It is very, very tempting to take the easy way out and use `component-local-state`. 
But you'll pay for it later as your app grows 
in complexity.   `component-local-state` is the "easy" approach.
`subscribed-component-state` is the __simple__ approach.

Fortunately, `Rocinante` takes care of eleven of these files for you, and you only need to create up to five files:

- `some.lib.events.user` (if you're creating `user` related events)
- `some.lib.handlers.user` (if you're creating `user` related handlers)
- `some.lib.procs.user` (if you're creating `user` related background processes)
- `some.lib.reactions.user` (if you're creating `user` related data graphs)
- `some.lib.data.user` (if you're creating `user` related data manipulation functions)


Additionally, if you update `some.lib.events.<namespace>`,
then you need to add a multimethod in `some.lib.events`.  
The same holds true for `some.lib.handlers.<namespace>` and
`some.lib.handlers`.

If you've gone to the trouble to set all this up, congratulations -- 
you've gone full `tilt` -- completely decoupled `events`, `handlers`, 
background `procedures`, `data` operations, and `subscriptions` 
into component state.


### What the hell...?

What on earth could justify 16 files for one component?

For your hard work you get:

* Testing -- all events generate maps.  Maps are testable, 
component local state is not.
* REPL -- you can change component state from the REPL
* WebWorkers can update component state after doing 
huge computations without UI lockup.
* WebSockets can change component state.
* Sanity -- it becomes possible to reason about what large applications are doing.
* Maintainability -- the code is easy to find, easy to read. 
Concerns are separated.  Changing code in one place will not 
break code in another place.
* Reactive datascript powered application: Leverage datascript
to its fullest potential. 
* Code splitting -- as your application becomes large, it's simple
to reason about breaking up your large compiled file into
smaller modules and loading lazily.
* Scalability -- it's simple to add new features without increasing complexity.  It's all just events. 
* Design -- work from an "event first" design perspective.  Think
about the interplay of events and how they will change the data -- the rest of the application will follow.
* It's Functional!  Component local state and tightly coupled side-effects
are the antithesis of functional design. If you've invested this heavily in ClojureScript and functional development 
for the browser, you should reap the full benefits of your hard work.

### Changes for Deployment

The primary change required to make this deployable is to reduce the size of the `webpack` bundle.
Once you're finished prototyping, change your `index.js` and `:requires` to be as specific as possible to
lower the javascript payload size.  Use code splitting and load lazily if necessary.  

## Why not...

### Re-frame?
[Re-frame](https://github.com/Day8/re-frame) is fantastic SPA framework, especially when used in conjunction with Eric Norman's [Understanding Re-frame](https://purelyfunctional.tv/courses/understanding-re-frame/) course.  However, I prefer using datascript as a primary datasource rather than re-frame's app-db.  Additionally, re-frame is its own DSL, relying on functions and macros.  When it came time for me to integrate websockets, webworkers, and even simple Ajax, re-frame fell short for me.  In order to do it the "re-frame way" required re-frame plugins, which, while fantastic, took me further down the re-frame rabbit hole.  
### Re-posh?
[Re-posh](https://github.com/denistakeda/re-posh) is a great plugin for re-frame that swaps the `re-frame.db/app-db` with a `posh` db, effectively giving you datascript for your re-frame application.  The only problem is, it's one more layer of DSL on top of the re-frame DSL, and now you're in a strange position that's not _quite_ re-frame DSL and it's not _quite_ datascript DSL.  Additionally, re-posh is an incomplete subset of datascript.  Certain things, like aggregations, just don't work like they do in datascript.  The differences are not well documented.  This frustration is ultimately what led me to abandon re-frame and re-posh in favor of the simple reagent approach.
### Posh?
[Posh](https://github.com/mpdairy/posh) is, on paper, reactive datascript.  Unfortunately it suffers from performance problems with large datasets, the code itself is quite complex, and it is a limited subset of datascript (does not support query aggregation).  In addition, the filters just don't quite work reliably or intuitively to me and finding workarounds are difficult.  `Rocinante`'s reactive datascript in conjunction with `reagent.ratom/make-reaction` is a simple, intuitive way to provide reactive denormalized data.  


## Roadmap

* [ ] Move certain verbose code into libraries (i.e., reactive datascript)
* [ ] IndexedDB `:data` operations
* [ ] WebSocket starter kit  
* [ ] clj-new.generate tools (to deploy domain specific code automatically)
* [ ] Simple API for sending tasks to WebWorkers
* [ ] Better documentation with more visuals (if only there was some kind of [pitch](https://pitch.com/) 
web application...)
* [ ] SASS/Garden
* [ ] Expanded SPA tutorial
