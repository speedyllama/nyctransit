Interaction Model Document
==========================
This file describes Interaction Model settings for Alexa.

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
hotel
india
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

Sample Utterances
-----------------
QueryTrainStatus What is the status of {Train} train
