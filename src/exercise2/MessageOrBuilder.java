// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Message.proto

package exercise2;

public interface MessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:exercise2.Message)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional int32 fr = 1;</code>
   * @return Whether the fr field is set.
   */
  boolean hasFr();
  /**
   * <code>optional int32 fr = 1;</code>
   * @return The fr.
   */
  int getFr();

  /**
   * <code>optional string msg = 2;</code>
   * @return Whether the msg field is set.
   */
  boolean hasMsg();
  /**
   * <code>optional string msg = 2;</code>
   * @return The msg.
   */
  java.lang.String getMsg();
  /**
   * <code>optional string msg = 2;</code>
   * @return The bytes for msg.
   */
  com.google.protobuf.ByteString
      getMsgBytes();

  /**
   * <code>optional int32 to = 3;</code>
   * @return Whether the to field is set.
   */
  boolean hasTo();
  /**
   * <code>optional int32 to = 3;</code>
   * @return The to.
   */
  int getTo();
}
