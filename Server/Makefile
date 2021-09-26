BUILD_FOLDER = build
MAIN_FILE = ApplicationServeur
SRC_FOLDER = src

PORT = 8080
INPUT_FOLDER = input/
CLASS_FOLDER = class/
OUTPUT_FILE = tmp.txt

default:
	cd $(SRC_FOLDER) && $(MAKE)
	mkdir -p $(BUILD_FOLDER)
	mv $(SRC_FOLDER)/*.class $(BUILD_FOLDER)

clean:
	rm -rf $(BUILD_FOLDER)

run: classes
	cd $(BUILD_FOLDER) && java $(MAIN_FILE) $(PORT) $(INPUT_FOLDER) $(CLASS_FOLDER) $(OUTPUT_FILE)

.PHONY: default classes clean run