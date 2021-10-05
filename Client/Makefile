BUILD_FOLDER = build
MAIN_FILE = ApplicationClient
SRC_FOLDER = src

HOST = localhost
PORT = 8080
COMMAND_FILE = ../command.txt
OUTPUT_FILE = ../output.txt

default:
	cd $(SRC_FOLDER) && $(MAKE)
	mkdir -p $(BUILD_FOLDER)
	mv $(SRC_FOLDER)/*.class $(BUILD_FOLDER)

clean:
	rm $(BUILD_FOLDER)/*.class

run: classes
	cd $(BUILD_FOLDER) && java $(MAIN_FILE) $(HOST) $(PORT) $(COMMAND_FILE) $(OUTPUT_FILE)

.PHONY: default classes clean run