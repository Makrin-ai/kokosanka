from json import loads 


class Config:
    def __init__(self, resolution: str, prompt: str):
        self.resolution: str = resolution
        self.prompt: str = prompt


def get_config(path: str = 'config.json') -> Config:
    with open(path) as file:
        data = loads(file.read())
        return Config(
            resolution=data['resolution'],
            prompt=data['prompt']
        )