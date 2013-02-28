#!/usr/bin/env python3
## BEEM is a videoconference application on the Android Platform.
## Copyright (C) 2009-2012 by Frederic-Charles Barthelery,
##			   Nikita Kozlov,
##			   Vincent Veronis.
##
## This file is part of BEEM.
##
## BEEM is free software: you can redistribute it and/or modify
## it under the terms of the GNU General Public License as published by
## the Free Software Foundation, either version 3 of the License, or
## (at your option) any later version.
##
## BEEM is distributed in the hope that it will be useful,
## but WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
## GNU General Public License for more details.
##
## You should have received a copy of the GNU General Public License
## along with BEEM.  If not, see <http://www.gnu.org/licenses/>.
##
## Please send bug reports with examples or suggestions to
## contact@beem-project.com or http://www.beem-project.com/

import urllib.request
import argparse, sys
from xml.dom.minidom import parse, getDOMImplementation


def getJidsFromUrl(url):
    f = urllib.request.urlopen(url)
    indoc = parse(f)
    jids = []
    query = indoc.documentElement
    if query.localName == "query" :
        for item in query.getElementsByTagName("item"):
            jid = item.getAttribute("jid")
            jids.append(jid)
    return jids

def createDocument():
    impl = getDOMImplementation()
    return impl.createDocument(None, "resources", None)


def purge(elems):
    res = []
    for i in elems:
        if i not in res:
            res.append(i)
    return res

def appendStringElem(doc, node, jids):
    for i in jids:
        item = doc.createElement("item")
        text = doc.createTextNode(i)
        item.appendChild(text)
        node.appendChild(item)


parser = argparse.ArgumentParser(description='Collect some free xmpp services')
parser.add_argument('url', metavar="url",
        default=['http://xmpp.net/services.xml', "https://list.jabber.at/api/?format=services.xml"] , nargs='*',
        help='url to get the services')

parser.add_argument('-o', metavar="FILE", type=argparse.FileType('bw'),
        default = sys.stdout.buffer,
        help='send output to FILE')

args = parser.parse_args()

# collect the servers jid
jids = []
for url in args.url:
    jids += getJidsFromUrl(url)

jids = purge(jids)

# create the xml output document
outdoc = createDocument()
res_element = outdoc.documentElement
string_array_elem = outdoc.createElement('string-array')
res_element.appendChild(string_array_elem)
string_array_elem.setAttribute("name", "xmpp_server_list")
appendStringElem(outdoc, string_array_elem, jids)

# print result
f = args.o
f.write(outdoc.toprettyxml(encoding="utf-8"))

