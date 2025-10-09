import os

from src import CommandHandler, get_config


def expand_env_vars(s: str) -> str:
    return os.path.expandvars(s)


config = get_config()

def main():
    handler = CommandHandler(os.getcwd(), print)
    global config

    while True:
        command, *args = input(config.prompt).split()
        handler.handle(command, args)


if __name__ == "__main__":
    main()
