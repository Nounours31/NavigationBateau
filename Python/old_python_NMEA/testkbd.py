from pynput import keyboard

import os
import time

def on_press(key):
    try:
        print('alphanumeric key {0} pressed'.format(
            key.char))
    except AttributeError:
        print('special key {0} pressed'.format(
            key))

def on_release(key):
    print('{0} released'.format(
        key))
    if key == keyboard.Key.esc:
        # Stop listener
        return False

# Collect events until released
def main():
    with keyboard.Listener(
        on_press=on_press,
        on_release=on_release) as listener:
            listener.join()

    while True:
        print ("Coucou")
        time.sleep(2)

if __name__ == "__main__":
    main()