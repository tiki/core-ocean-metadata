ENVIRONMENT ?=default
SAM_CLI_TELEMETRY ?=0
export SAM_CLI_TELEMETRY

.PHONY: help
help:
	@echo "Look at the Makefile"

.PHONY: lint
lint: lint-code lint-infra

.PHONY: build
build: build-code build-infra

.PHONY: lint-code
lint-code:
	@echo "No Java linter configured yet"

.PHONY: lint-infra
lint-infra:
	sam validate --config-env "${ENVIRONMENT}"

.PHONY: build-utils
build-utils:
	cd utils && mvn package install
	cd utils && mvn package

.PHONY: build-code
build-code: build-utils
	mvn package

.PHONY: build-infra
build-infra:
	sam build --config-env "${ENVIRONMENT}"

.PHONY: deploy
deploy:
	sam deploy --config-env "${ENVIRONMENT}"
