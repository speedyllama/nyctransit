NYC Transit
==========

An Alexa Skill for Amazon Echo to query New York City subway status

Invocation Name
---------------
n. y. c. transit

Short Skill Description
-----------------------
Ask me New York City subway status.

Full Skill Description
----------------------
New York City subway system is not reliable sometimes, so ask Alexa before you head to the station!
You can ask me New York City subway status. Like:
What is the status of 7?
For alphabetical trains, like A, C, E trains, use a word that begins with that alphabet instead.
This is to improve voice recognition.
For example, for A train, say:
What is the status of Alpha? 
For B train, say:
What is the status of Bravo? 
NATO phonetic alphabets are recommended:
A for Alpha.
B for Bravo.
C for Charlie.
D for Delta.
E for Echo.
F for Foxtrot.
G for Golf.
J for Juliette.
L for Lima.
M for Mike.
N for November.
Q for Quebec.
R for Romeo.
S for Sierra.
Z for Zulu.
Also, you may say Shuttle for S train. 
And, please say the full name: Staten Island Rail, not S. I. R., for Staten Island Rail.

We hope this Alexa skill helps you commute. If you encounter problems, please do not hesitate to contact us:
info@speedyllama.com

Thank you!

* This Alexa Skill is not affiliated with MTA.

Example Phrases
---------------
Alexa, ask NYC Transit what is the status of 7?
Alexa, ask NYC Transit what is the status of Alpha?
Alexa, ask NYC Transit what is the status of Bravo?

Category
--------
Travel

Keywords
--------
subway New York City NYC train transit

Intent Schema
------------
{
  "intents": [
    {
      "intent": "QueryTrainStatus",
      "slots": [
        {
          "name": "Train",
          "type": "TRAIN"
        }
      ]
    }
  ]
}

Custom Slot Types
----------------
### Name
TRAIN

### Value
one
two
three
four
five
six
seven
alpha
bravo
charlie
delta
echo
foxtrot
golf
juliette
kilo
lima
mike
november
quebec
romeo
shuttle
sierra
zulu
staten island rail

Sample Utterances
-----------------
QueryTrainStatus What is the status of {Train} train
QueryTrainStatus What is the status of {Train}
QueryTrainStatus What's the status of {Train} train
QueryTrainStatus What's the status of {Train}
