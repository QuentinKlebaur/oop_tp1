BUILD_FOLDER = build
MAIN_FILE = ApplicationClient
SRC_FOLDER = src

HOST = localhost
PORT = 8080
COMMAND_FOLDER = ../command.txt
OUTPUT_FILE = ../output.txt

default:
	cd $(SRC_FOLDER) && $(MAKE)
	mkdir -p $(BUILD_FOLDER)
	mv $(SRC_FOLDER)/*.class $(BUILD_FOLDER)

clean:
	rm -rf $(BUILD_FOLDER)

run: classes
	cd $(BUILD_FOLDER) && java $(MAIN_FILE) $(HOST) $(PORT) $(COMMAND_FOLDER) $(OUTPUT_FILE)

.PHONY: default classes clean run