import os 


class CommandHandler:
    def __init__(self, cwd: str, write_line):
        self.cwd = cwd
        self.write_line = write_line

    def handle(self, name: str, args: list):
        full_command = [name, *args]
        new_command = []

        for command in full_command:
            if '$' in command:
                result = os.environ.get(command.replace('$', ''), command)
                new_command.append(result)
            else:
                new_command.append(command)
        
        name, *args = new_command

        if args:
            self.write_line(f"Команда: {name} с аргументами: {args}")

        elif name == 'exit':
            exit(0)
        else:
            self.write_line(f"Команда: {name} без аргументов")
        
        if name == "cd":
            self.write_line(f"Команда cd; текущая CWD: {self.cwd}")