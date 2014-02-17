# Makefile - All credit to UC Berkeley's Paul Hilfinger for this 
# Makefile's format. I only changed the wording to make it work
# for my directory

# This a Makefile, an input file for the GNU 'make' program.  For you 
# command-line and Emacs enthusiasts, this makes it possible to build
# this program with a single command:
#     make 
# You can also clean up junk files and .class files with
#     make clean
# To run style61b (our style enforcer) over your source files, type
#     make style
# Finally, you can run any tests you'd care to with
#     make check
# This first runs your program on the input files tests/*.inp and checks
# to see that it completes normally and that the results are those in the
# files tests/*.out.  It also runs the program on the input files tests/*.err
# and checks that the program properly reports an error as given in the
# specification. It's up to you to come up with the test files; the 
# skeleton just includes some simple samples.

SHELL = bash

STYLEPROG = style61b

TEST_CORRECT = ./test-correct

TEST_ERROR = ./test-error

SRC = $(wildcard CPMstats/*.java)

TESTS = tests
CORRECT_TESTS = $(TESTS)/correct/*.inp
ERROR_TESTS = $(TESTS)/error/*.inp

.PHONY: default style check clean

default: CPMstats/Main.class

CPMstats/Main.class: $(SRC)
	javac -g $(SRC)

style:
	$(STYLEPROG) CPMstats/*.java

check: CPMstats/Main.class
	@code=0; \
	echo "Testing correct inputs:"; \
	if ! $(TEST_CORRECT) $(CORRECT_TESTS); then code=1; fi; \
	echo; echo "Testing erroneous inputs:"; \
	if ! $(TEST_ERROR) $(ERROR_TESTS); then code=1; fi; \
	test $$code -eq 0

clean:
	$(RM) CPMstats/*.class */*~ *~ OUT


