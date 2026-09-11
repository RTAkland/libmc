import json
import os

path = "./registries.json"

with open(path, "r") as f:
    data = json.loads(f.read())["minecraft:particle_type"]

sorted_entries = sorted(
    data.get('entries', {}).items(),
    key=lambda item: item[1].get('protocol_id', 0)
)

for raw_key, value in sorted_entries:
    identifier = raw_key.replace('minecraft:', '', 1)

    if ':' in identifier:
        parts = identifier.split(':', 1)
        name_upper = parts[1].upper()
        formatted_name = f"{parts[0]}_{name_upper}"
    else:
        formatted_name = identifier.upper()

    proto_id = value.get('protocol_id')
#     print(f"{formatted_name}({proto_id}, \"{raw_key}\"),")
    print(f"{formatted_name},")