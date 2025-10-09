class CommandHandler:
    def __init__(self, cwd: str, write_line):
        self.cwd = cwd
        self.write_line = write_line

    def handle(self, name: str, args: list):
        if args:
            self.write_line(f"Команда: {name} с аргументами: {args}")
        else:
            self.write_line(f"Команда: {name} без аргументов")
        
        if name == "cd":
            self.write_line(f"Команда cd; текущая CWD: {self.cwd}")