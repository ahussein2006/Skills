package com.code.test;

public class Test {
    public void test() {
	System.out.println("Test");
    }

    public void testA() {
	System.out.println("A");
    }

    public void testB() {
	System.out.println("B");
    }

    public void testC() {
	System.out.println("C");
    }

    public void testD100() {
	System.out.println("D100100");
    }

    public void testD200() {
	System.out.println("D200300");
    }

    public void testRef() {
	System.out.println("test origin head");
    }

    public void testFetch() {
	System.out.println("testFetch");
    }

    public void testTag() {
	System.out.println("testTagAgain");
    }

    public void testLocalRebase() {
	System.out.println("testLocalRebase");
    }

    public void testPrepareDetatchedHead() {
	System.out.println("testPrepareDetatchedHead");
    }

    public void testConflictWithRemoteTrackingBranch() {
	System.out.println("testConflictWithRemoteTrackingBranch");
    }

    public void rebaseSameBranch() {
	System.out.println("conflict ok");
    }

    public void rebaseSameBranchCommitThree() {
	System.out.println("rebaseSameBranchCommit3");
    }

    public void testRebaseCommitOne() {

    }

    public void testRebaseCommitTwo() {

    }

    public void useRebaseCommitOne() {
	testRebaseCommitOne();
	testRebaseCommitOne();
    }

    public void useRebaseCommitTwo() {
	testRebaseCommitTwo();
	testRebaseCommitTwo();
    }

    public void useRebaseCommitThree() {
	rebaseSameBranchCommitThree();
	rebaseSameBranchCommitThree();
    }

    public void testBranch2() {
	System.out.println("testBranch2");
	System.out.println("testBranch2");
    }

    public void testBranch3() {
	System.out.println("testBranch3");
	System.out.println("testBranch3");
    }

    public void testRebaseBranch() {
	System.out.println("testRebaseBranch");
    }
}
