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
12/31/2016 Updated to Version 1.1:
"W" train support added.

This is an open source project! Issue reports, forks and pull requests are always welcome!
https://github.com/speedyllama/nyctransit

Response To Comments:

Dear users,

Thank you for using NYC Transit Echo App. Special thanks to those who left a comment. 
From the comments, we understand that there are three common issues:

1. The intent sentence is wordy.
2. A word rather than a single character is needed for alphabetical trains.
3. Recognizing might not be accurate sometimes.

To be frank, these issues are mostly due to one single reason: the official Amazon Echo SDK does not support alphabet recognition.
For this reason, we tried our best to figure out the best combination of intent words to accommodate alphabetical trains.
And, the intent sentence we have been using is the best so far.
If your voice is not recognized accurately, try using a different word. And, always stick to the intent sentence.
Like, say:

What is the status of Alpha?

instead of:

What is the status of Alpha train?

The extra word "train" at the end of the sentence might confuse Alexa.

Meanwhile, it has been almost one year since the initial release and there is a great chance that Amazon updated their SDK.
Thus, we might be able to find a better way to deal with the alphabet recognition issue and the user experience could be significantly improved.

Will keep you posted! Thank you again for using the app!

1/5/2016 Version 1.0

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
W for Whiskey.
Z for Zulu.
Also, you may say Shuttle for S train. 
And, please say the full name: Staten Island Rail, not S. I. R., for Staten Island Rail.

We hope this Alexa skill helps you commute. If you encounter problems, please do not hesitate to contact us:
info@speedyllama.com

For other projects made by Speedy Llama, please visit:
https://www.speedyllama.com/

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

Testing Instructions
-------------------
You can check NYC subway status from this site: http://www.mta.info/
NYC Transit Alexa Skill syncs with this site every minute.
Use NATO phonetic alphabets for alphabetical trains, like A, C, E trains. Read the full description for details.

Thanks!

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
      "intent": "AMAZON.HelpIntent"
    },
    {
      "intent": "AMAZON.YesIntent"
    },
    {
      "intent": "AMAZON.NoIntent"
    },
    {
      "intent": "AMAZON.CancelIntent"
    },
    {
      "intent": "AMAZON.StopIntent"
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
whiskey
zulu
staten island rail

Sample Utterances
-----------------
QueryTrainStatus What is the status of {Train} train
QueryTrainStatus What is the status of {Train}
QueryTrainStatus What's the status of {Train} train
QueryTrainStatus What's the status of {Train}
