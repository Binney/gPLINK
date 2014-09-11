gPLINK: a GUI wrapper for PLINK allowing graphical creation of complex commands and queues.

All credit goes to Kathe Todd-Brown and colleagues at Harvard who created this software. I just hacked it together to make it do slightly more stuff.

Queues
======

The main addition to GPLINK is the queue facility. You can create commands graphically, put them in a queue and rearrange them, then hit OK. Each command will then be executed in turn (starting when the previous one finished). You can also import queues from a file, meaning common processes can be automated easily.

To make a queue, go to the menu bar and click Queue > New. Add commands using the Add button. You can graphically create commands using the PLINK interface, or simply enter your own directly into the command field. Rearrange commands using the move up and move down buttons, or delete them. Once you're done, click OK to save the queue. You can then run it immediately or save it for later.

Saved queues appear in the Operations pane on the main GPLINK window. You can run them by right-clicking on them and selecting Run. If they're currently running there'll be a Running icon next to it, or a green tick for successfully completed ones. To find out how far a queue is done, click the arrow on the left and you'll see its component commands along with which one is currently running.

You can also right-click on a queue to save it to a file for later importing.

Import queues
=============

Go to the Queue menu and select Import. You can now load previously saved queues into the queue creation interface. This allows you to modify the queue, 
for example by reordering commands or changing parameters. Be sure to change the input file names to something suitable.