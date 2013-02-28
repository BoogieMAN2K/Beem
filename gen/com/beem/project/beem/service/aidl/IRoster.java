/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/victorhugogil/Documents/Eclipse Workspace/Login/src/com/beem/project/beem/service/aidl/IRoster.aidl
 */
package com.beem.project.beem.service.aidl;
public interface IRoster extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.beem.project.beem.service.aidl.IRoster
{
private static final java.lang.String DESCRIPTOR = "com.beem.project.beem.service.aidl.IRoster";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.beem.project.beem.service.aidl.IRoster interface,
 * generating a proxy if needed.
 */
public static com.beem.project.beem.service.aidl.IRoster asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.beem.project.beem.service.aidl.IRoster))) {
return ((com.beem.project.beem.service.aidl.IRoster)iin);
}
return new com.beem.project.beem.service.aidl.IRoster.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_addContact:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String[] _arg2;
_arg2 = data.createStringArray();
boolean _result = this.addContact(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_deleteContact:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.Contact _arg0;
if ((0!=data.readInt())) {
_arg0 = com.beem.project.beem.service.Contact.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.deleteContact(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getContact:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
com.beem.project.beem.service.Contact _result = this.getContact(_arg0);
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_setContactName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.setContactName(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_createGroup:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.createGroup(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_addContactToGroup:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.addContactToGroup(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_removeContactFromGroup:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.removeContactFromGroup(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_getContactList:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<com.beem.project.beem.service.Contact> _result = this.getContactList();
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_getGroupsNames:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<java.lang.String> _result = this.getGroupsNames();
reply.writeNoException();
reply.writeStringList(_result);
return true;
}
case TRANSACTION_getPresence:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
com.beem.project.beem.service.PresenceAdapter _result = this.getPresence(_arg0);
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_addRosterListener:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.aidl.IBeemRosterListener _arg0;
_arg0 = com.beem.project.beem.service.aidl.IBeemRosterListener.Stub.asInterface(data.readStrongBinder());
this.addRosterListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_removeRosterListener:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.aidl.IBeemRosterListener _arg0;
_arg0 = com.beem.project.beem.service.aidl.IBeemRosterListener.Stub.asInterface(data.readStrongBinder());
this.removeRosterListener(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.beem.project.beem.service.aidl.IRoster
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public boolean addContact(java.lang.String user, java.lang.String name, java.lang.String[] groups) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(user);
_data.writeString(name);
_data.writeStringArray(groups);
mRemote.transact(Stub.TRANSACTION_addContact, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void deleteContact(com.beem.project.beem.service.Contact contact) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((contact!=null)) {
_data.writeInt(1);
contact.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_deleteContact, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public com.beem.project.beem.service.Contact getContact(java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.beem.project.beem.service.Contact _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_getContact, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.beem.project.beem.service.Contact.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setContactName(java.lang.String jid, java.lang.String name) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jid);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_setContactName, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void createGroup(java.lang.String groupname) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(groupname);
mRemote.transact(Stub.TRANSACTION_createGroup, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void addContactToGroup(java.lang.String groupName, java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(groupName);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_addContactToGroup, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void removeContactFromGroup(java.lang.String groupName, java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(groupName);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_removeContactFromGroup, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.util.List<com.beem.project.beem.service.Contact> getContactList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.beem.project.beem.service.Contact> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getContactList, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.beem.project.beem.service.Contact.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.util.List<java.lang.String> getGroupsNames() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<java.lang.String> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getGroupsNames, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArrayList();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public com.beem.project.beem.service.PresenceAdapter getPresence(java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.beem.project.beem.service.PresenceAdapter _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_getPresence, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.beem.project.beem.service.PresenceAdapter.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void addRosterListener(com.beem.project.beem.service.aidl.IBeemRosterListener listen) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listen!=null))?(listen.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addRosterListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void removeRosterListener(com.beem.project.beem.service.aidl.IBeemRosterListener listen) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listen!=null))?(listen.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_removeRosterListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_addContact = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_deleteContact = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getContact = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_setContactName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_createGroup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_addContactToGroup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_removeContactFromGroup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getContactList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getGroupsNames = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_getPresence = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_addRosterListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_removeRosterListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
}
public boolean addContact(java.lang.String user, java.lang.String name, java.lang.String[] groups) throws android.os.RemoteException;
public void deleteContact(com.beem.project.beem.service.Contact contact) throws android.os.RemoteException;
public com.beem.project.beem.service.Contact getContact(java.lang.String jid) throws android.os.RemoteException;
public void setContactName(java.lang.String jid, java.lang.String name) throws android.os.RemoteException;
public void createGroup(java.lang.String groupname) throws android.os.RemoteException;
public void addContactToGroup(java.lang.String groupName, java.lang.String jid) throws android.os.RemoteException;
public void removeContactFromGroup(java.lang.String groupName, java.lang.String jid) throws android.os.RemoteException;
public java.util.List<com.beem.project.beem.service.Contact> getContactList() throws android.os.RemoteException;
public java.util.List<java.lang.String> getGroupsNames() throws android.os.RemoteException;
public com.beem.project.beem.service.PresenceAdapter getPresence(java.lang.String jid) throws android.os.RemoteException;
public void addRosterListener(com.beem.project.beem.service.aidl.IBeemRosterListener listen) throws android.os.RemoteException;
public void removeRosterListener(com.beem.project.beem.service.aidl.IBeemRosterListener listen) throws android.os.RemoteException;
}
