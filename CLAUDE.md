# Hexagonal Chess

## Project Overview

This is an example project to demonstrate prinicples of Hexagonal Architecture, using an online chess game as its sample application.

## Architecture

This repository contains sample projects in different languages, including:
- Java
- Python
- Ruby

In each case, the same principles are demonstrated. In particular, there are two bounded contexts:
- Gameplay, concerned with the rules of chess
- Organizing Games, concerned with organizing games between friends.

Each project demonstrates strict adherence to Hexagonal Architecture. This means that for each of the two bounded context, there is a Domain module that defines data classes, operations, and interfaces, Primary Adaptors that invoke operations in the Domain, Secondary Adaptors that implement interfaces from the Domain, and a single Deployable module that wraps up all of the bounded contexts into a single deployable application.

In adherence to Hexagonal Architecture principles:
- The Domain module may not have any dependencies on anything outside of itself.
- The Adaptor modules may have dependencies on the Domain module, but not on the Deployable module.
- The Deployable module may depend on everything.


