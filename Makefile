JC = javac
JFLAGS = -g -d bin -sourcepath src
MAIN = Main

# Find all java files in the src directory
SOURCES = $(shell find src -name "*.java")

all: build

build:
	@mkdir -p bin
	$(JC) $(JFLAGS) $(SOURCES)

run: build
	java -cp bin $(MAIN)

clean:
	rm -rf bin

.PHONY: all build run clean
