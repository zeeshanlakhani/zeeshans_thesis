# **Visualizing the Temporal Nature of Music: Extracting, Matching, Clustering, and Representing Chord Progressions** - NYU Thesis, Fall 2009


# What is this repo?
This repository contains the original code (completed in [Processing](http://processing.org) and Java), thesis paper, and executable application for my thesis project, completed and defended for NYU's Music Technology program. The pdf details the background, impetus, and programming structure of the work. 

Basically, it works in this way:
> 1. An offline database of chordal material (12 major and 12 minor chords) accumulated via the extraction of chroma features and the use of Hidden Markov Models from a set of remixes that pertain to one original track in this case. This database is linked to via... 

> 2. a simple search engine that allows a user to call searches for specific chord progressions (one chord or multiple) based on either the comparison of the original track to its remixed offspring or on pure curiosity about the usage of certain progressions. Once a search is undergone,...

> 3. the chosen pattern becomes the root for a visualization that bifurcates from the left and right of it, clustering the rest of the chords (textual strings) into lexicographically arranged subsequences/substrings. This visual representation aims to show the harmonic variations of the data with the user’s keychord (like keyword) acting as the parent that all the other substrings (offspring) must share. The visual itself is text-based, depicting chords as their letter components. Parts 2 and 3 work in real-time.