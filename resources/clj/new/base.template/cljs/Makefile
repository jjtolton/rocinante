setup:
		yarn init --yes
		bash setup.sh
		yarn webpack

repl: clean
		clj -R:nREPL -m nrepl.cmdline --middleware "[cider.piggieback/wrap-cljs-repl]"

nrepl:
		clj -Anrepl-server

clean:
		rm -rf resources/public/js/compiled
		rm -rf out
		rm -rf target

build: clean
		clj -R:nREPL -m figwheel.main -O advanced -bo dev
		clj -R:nREPL -m figwheel.main -O advanced -bo worker

runner:
		clj -Arunner

run:
		clj -A:run


FORCE:
