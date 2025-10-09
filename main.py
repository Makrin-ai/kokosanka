import os

import tkinter as tk
from tkinter import scrolledtext, messagebox

from src import CommandHandler, get_config


def expand_env_vars(s: str) -> str:
    return os.path.expandvars(s)


class VFSReplApp:
    def __init__(self, root: tk.Tk):
        self.config = get_config()
        root.geometry(self.config.resolution)

        self.root = root
        self.root.title("VFS — эмулятор оболочки")
        self.cwd = os.getcwd()

        self.handler = CommandHandler(self.cwd, self.write_line)

        self.text = scrolledtext.ScrolledText(root, wrap=tk.WORD, state=tk.DISABLED, font=("Courier", 10))
        self.text.pack(fill=tk.BOTH, expand=True, padx=6, pady=6)

        bottom_frame = tk.Frame(root)
        bottom_frame.pack(fill=tk.X, padx=6, pady=(0,6))

        self.prompt_label = tk.Label(bottom_frame, text=self.config.prompt, font=("Courier", 10))
        self.prompt_label.pack(side=tk.LEFT)

        self.entry = tk.Entry(bottom_frame, font=("Courier", 10))
        self.entry.pack(fill=tk.X, side=tk.LEFT, expand=True)
        self.entry.bind("<Return>", self.on_enter)
        self.entry.focus_set()

        self.write_line("REPL прототип. Введите 'exit' для выхода")
        self.write_line(f"Текущая директория: {self.cwd}")
        self.write_line("Пример: ls -la $HOME ; cd /tmp ; echo $PATH")
        self.write_prompt()

    def write(self, text: str):
        self.text.configure(state=tk.NORMAL)
        self.text.insert(tk.END, text)
        self.text.see(tk.END)
        self.text.configure(state=tk.DISABLED)

    def write_line(self, text: str = ""):
        self.write(text + "\n")

    def write_prompt(self):
        self.write(self.config.prompt)

    def on_enter(self, event=None):
        raw = self.entry.get()
        self.write_line(raw)
        self.entry.delete(0, tk.END)

        if raw.strip() == "":
            self.write_line()
            self.write_prompt()
            return

        try:
            expanded = expand_env_vars(raw)
        except Exception as e:
            self.write_line(f"Error expanding environment variables: {e}")
            self.write_prompt()
            return

        try:
            args = expanded.split()
        except ValueError as e:
            self.write_line(f"Parsing error: {e}")
            self.write_prompt()
            return

        if not args:
            self.write_prompt()
            return

        cmd, *cmd_args = args

        self.handler.handle(cmd, cmd_args)
        self.write_prompt()

    


def main():
    root = tk.Tk()
    app = VFSReplApp(root)

    try:
        root.mainloop()
    except KeyboardInterrupt:
        try:
            root.quit()
        except Exception:
            pass


if __name__ == "__main__":
    main()
