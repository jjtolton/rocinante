setup:
		bash setup.sh

repl: clean
		clj -R:nREPL -m nrepl.cmdline --middleware "[cider.piggieback/wrap-cljs-repl]"

nrepl:
		clj -Anrepl-server

clean:
		rm -rf resources/public/js/compiled
		rm -rf out

build: clean
		clj -m cljs.main -co compiler-opts.edn --compile

runner:
		clj -Arunner

run:
		clj -A:run


FORCE:
