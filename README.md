# rocket-scientist-master
A concurrency project that me and my partner did for a module.
The main concept of this project is there's several exploration missions sent into space all communicating with mission control, generating and sending reports to mission control. Some reports require a response from mission control  
Each mission/space craft has 3 connections that it can send reports over, each with varying bandwidth that can only send 1 report at a time.  
We devised a method to ensure that all reports are eventually sent through first in first out fairness using queues.  
We used threadpools to simulate the missions and the network bandwidths to ensure the program is as efficient as possible.

