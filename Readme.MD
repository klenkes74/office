# Kaiserpfalz EDV-Service Office

> My brother once asked me if I want to finish my pet project or if I want to crown it. Well, I think I decided to 
> crown it.
>
> -- Roland T. Lichti about KP Office in the year 2009

## Abstract
The KP Office system is a plugable administration server. It contains basic functions like 

- Contact Data Management,
- Containers to organize data in it called "Folders",

but it relies heavily on generic services provided by other much bigger projects like

- OIDC authentication/authorization (I use keycloak as default implementation),
- Hibernate with Panache, ...
- Quarkus as startup/integration framework.

Future plans (errr, I have them since around 1998) contain general usable modules like

- Accounting (mainly general ledger). The general modules will be LGPL, there may be accounting related modules that
  will be closed source (please read the reasoning in chapter "License" below),
- Handling of Snailmail (for small business like tax advisors, law offices, ...)

but there are also ideas for special interest modules like

- event management for conventions (I'm a RPG guy and I miss good support for organizing/managing events)
- Backend for contact search
- Implementation of a multisite roleplaying chat resembling the Legend Of The Green Dragon system (but with a shared 
  realm for multiple sites)

## License
The license for the software is LGPL 3.0 or newer. 

There may be modules published by a subscription model, but I don't 
think that I will finish them soon. That would be mainly a module for the accounting part to provide special access 
interfaces to tax authorities. These modules need to be audited by the tax offices and they take a lot of money for it.
So either they will be created via Crowdfunding or you will have pay for it to cover the costs involved in creating the 
software. Or both: crowdfounding for the creation and subscription for the maintenance.

## Architecture

tl;dr (ok, only the bullshit bingo words):
- Deployment Kubernetes, ok, let's name it: OKD or OpenShift :-)
- Hexagonal Architecture
- Microservices
- REST
- Command Query Responsibility Segregation
- Immutable Objects
- Relying heavily on generated code
- 100 % test coverage of human generated code
- Every line of code not written is bug free!

The software will be delivered as bunch of Microservices. Currently I aim for a hexagonal architecture with northbound 
REST services heavily resembling a data structure like Kubernetes api objects (I like the rich metadata) and southbound
adapters for data storage and adapters to supplier services. The domain models in between is modeled with immutables.
I'm a strong believer in CQRS for big systems. Challenge me.

I'm aiming for deployments within OpenShift or OKD, K8s may be possible. So I want to use the infrastructure provided.
I don't directly care about logging, monitoring, alarming and reporting. I deliver logs to STDOUT/STDERR, metrics
endpoints for scraping by prometheus. So you can use default infrastructure.

Code test coverage for human generated code should be 100%, machinge generated code is considered bugfree until proven 
wrong. But every line that needs not be written is a bug free line without need to test it. So aim for not writing code.

### Microservice Overview
The system will consist of different microservices. There will be a bigger one providing the basic infrastructure 
(contacts, folders, tenants). And then there are the additional modules grouped around these services. Some domains are
not decided yet. A full featured address service (containing master file data for geolocation) may be offered as 
microservice of its own or may be attached to the contacts part of the base services. I'm still thinking about that.

#### Base Service
- contacts
- folders
- tenants
- export for status information publishing (like [cachet](https://cachethq.io)), I heed to have a look into
  [Cahcet-URL-Monitor](https://github.com/mtakaki/cachet-url-monitor/), perhaps that all may be done via configuration
  and no new code is needed.

#### Finance
- accounting
- controlling
- billing

#### DMS
- integration in DMS system

#### Mailroom
- documentation of outgoing and incomming mail
- ticker file (for resubmissions); especially in conjunction with the DMS

#### Event Organisation
- Announcing events
- selling tickets
- organizing venues with different rooms and talks (or gaming sessions)
- self-organization of venues with talks (or gaming sessions)
- providing data for heads-up
- alarming speaker

## A note from the author
Well, I had several attempts on this system (the first one in PHP3). But I have the feeling that now is the right time
and the toolsets are mighty enough to enable small teams (like the army of me) to deliver enough value to takle such a
big monster project. But if someone is interested in getting it faster, we may team up. I'm open for that. But be 
warned: I want to do it _right_. So no short cuts to get faster. And be prepared for some basic discussions about the
architecture or software design :-).

---
Bensheim, 2019-12-14-22:59Z