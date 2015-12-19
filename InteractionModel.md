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
    },
    {
      "intent": "QueryStatusDetail",
      "slots": [
        {
          "name": "Answer",
          "type": "LITERAL"
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
QueryTrainStatus What is the status of {Train}
QueryTrainStatus What's the status of {Train} train
QueryTrainStatus What's the status of {Train}
QueryTrainStatus {Train} train status
QueryStatusDetail {yes|Answer}
QueryStatusDetail {yeah|Answer}
QueryStatusDetail {ok|Answer}
QueryStatusDetail {go ahead|Answer}
