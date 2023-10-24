# Java edition in Eclipse IDE, using the Netbeans Language Server (nbcode)

This repository implements a client for Netbeans Language Server (nbcode) to provide edition of .java files in the Eclipse IDE. This can be used as an alternative to Eclipse JDT. Installation of Eclipse JDT is not required for this plugin to work; a local installation of `nbcode` must be available and referenced for this plugin to work.

## 💡 Motivation

Language Server Protocol has conquered the world of code edition; in Eclipse IDE, it's used in order to provide edition support for many language and technologies (see https://github.com/eclipse/lsp4e for details). [Netbeans's nbcode](https://github.com/apache/netbeans/tree/master/java/java.lsp.server/nbcode) is a mature language server serving popular dev tools (eg https://github.com/oracle/javavscode ).

The goal of this project is to give ability to try this LS in the Eclipse IDE and easily compare it with Eclipse JDT UI or [eclipseide-jdtls](https://github.com/redhat-developer/eclipseide-jdtls), to detect what are the main gaps and how those might be filled.

## ⚗️ Status: Experimental/Alpha

The current state of this software is Experimental/Alpha: it is currently working as a minimal viable product but it is not polished for general usage yet.

However, anyone is encouraged to try it (and to contribute): it's easy to install it, use it and uninstall it later; without risk of harming your IDE.

## 📥 Installation

You can install it in your Eclipse IDE or Eclipse RCP applicaiton using *Help > Install New Software* and pointing to https://mickaelistria.github.io/eclipseide-nbcode and installing the only installable item.

## 🏗️ Building

`mvn clean verify`; output p2 repo is then available for local installation in `repository/target/repository`

## ⌨️ Contributing

Please provide your contributions through the usual GitHub channels: PRs, Issues, Discussions...
