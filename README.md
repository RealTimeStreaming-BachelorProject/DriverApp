# SDK Version
This android application requires an SDK version of 30 or above.

# Idea behind the app
The app is meant for couriers driving around with packages. They are meant to send their GPS coordinates to the people waiting for their package to be delivered.
The courier can also scan packages.
This app is a part of a larger solution in cooperation with our bachelor project.

# Demo:
1. You are presented with a login screen. Write "test" in the username field, and click on the login button. This will trigger a demo version of the app to run with no server dependencies.
2. At the bottom there is a navigation menu with three screens, each their own fragment.
3. The first screen, is the 'GPS' screen. Here you can start collecting GPS Coordinates and send it to our server with an android foregroundservice.
4. The second screen is the 'packages' screen. This contains a list (recylcerView) of all the packages that you have picked up for delivery and have delivered. It will be empty at first.
5. The third sceen is the scanner screen. This is for demo purposes. Here you can emulate scanning a package. This package will be added to the list inside the second screen 'packages'. It can also emulate scanning a package for delivery. This will give a checkpoint inside the screen 'packages'.
