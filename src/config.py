from json import loads 


class Config:
    def __init__(self, prompt: str):
        self.prompt: str = prompt


def get_config(path: str = 'config.json') -> Config:
    with open(path) as file:
        data = loads(file.read())
        return Config(
            prompt=data['prompt']
        )