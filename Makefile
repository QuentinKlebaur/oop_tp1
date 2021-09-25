server_folder = Server
client_folder = Client

all: server client

server:
	cd $(server_folder) && $(MAKE)

client:
	cd $(client_folder) && $(MAKE)

server_clean:
	cd $(server_folder) && $(MAKE) clean

client_clean:
	cd $(client_folder) && $(MAKE) clean

clean: server_clean client_clean

run_server:
	cd $(server_folder) && $(MAKE) run

run_client:
	cd $(client_folder) && $(MAKE) run

.PHONY: client server client_clean server_clean clean all run_server run_client