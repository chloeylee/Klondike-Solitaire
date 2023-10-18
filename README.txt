Changes Made to Klondike Pt 1.
1. simplified deck creation: changed the initDeck() method to be a simplified for loop
   with less repetitive code
2. created rank and suit enumerations and added to CardBuilder rather than just using strings
3. removed unused/useless drawCard() method from BasicKlondike
4. made any methods in BasicKlondike that weren't in the interface private


NOTE:
Cascade piles are printed in a manner so that it is rotated 90º counter-clockwise and flipped
vertically. My apologies.


Changes Made to Klondike Pt 2.
1. made a ton of helpers for the controller for any repeated code (i.e. throwing exceptions)