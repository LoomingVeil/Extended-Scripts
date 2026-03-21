"""
For whatever reason, the public instance variables in com.veil.extendedscripts.constants
do not want to go into the typescript files so we will do this manually.
"""

import os

source_folder = os.path.join(os.getcwd(), "src", "main", "java", "com", "veil", "extendedscripts", "extendedapi", "constants")
target_folder = os.path.join(os.getcwd(), "src", "main", "resources", "assets", "extendedscripts", "api", "com", "veil", "extendedscripts", "extendedapi", "constants")

variables = {}
print(f"Looking through {len(os.listdir(source_folder))} files in {source_folder}")
for filename in os.listdir(source_folder):
    found_something_in_file = False
    if filename.endswith(".java"):
        variables_to_add = []
        with open(os.path.join(source_folder, filename), "r") as f:
            print(f"Reading {filename}")
            for line in f.readlines():
                if "=" not in line:
                    continue
                line = line.lstrip().replace("public final ", "")
                if line.startswith("int"):
                        variables_to_add.append({
                            "name": line.replace("int ", "").split(" ")[0] + ": number;",
                            "value": line.split(" = ")[1].replace(";", "").strip()
                        })
                        found_something_in_file = True
                elif line.startswith("char"):
                    variables_to_add.append({
                        "name": line.replace("char ", "").split(" ")[0] + ": string;",
                        "value": line.split(" = ")[1].replace(";", "").strip()
                    })
                    found_something_in_file = True
                elif line.startswith("String"):
                    variables_to_add.append({
                        "name": line.replace("String ", "").split(" ")[0] + ": string;",
                        "value": line.split(" = ")[1].replace(";", "").strip()
                    })
                    found_something_in_file = True

            if found_something_in_file:
                variables[filename.replace(".java", "")] = variables_to_add
            else:
                print(f"No variables found in {filename}")

for filename in variables:
    print(f"Fixing {filename}")
    current_text = ""
    with open(os.path.join(target_folder, filename + ".d.ts"), "r") as f:
        current_text = f.read()

    current_text = current_text[:-2]
    for variable in variables[filename]:
        current_text += f"\t/**\n\t * Value is {variable['value']}\n\t */\n"
        current_text += "\t" + variable["name"] + "\n"
    current_text += "}\n"

    with open(os.path.join(target_folder, filename + ".d.ts"), "w") as f:
        f.write(current_text)


