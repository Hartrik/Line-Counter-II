// comment 1

static void Main(string[] args) { // comment 2

    int number = 1 + 2; /*co
m
ment3*/

    string a =  "some /* // this is not comment";  // comment 4
    string b =  "hello \t world";
    string c =  "escape 1: \" text ";   // comment 5
    string d = @"escape 2: "" text ";  // comment 6
    string e = @"one
    two                 // this is not comment
    three";

    // comment 7

    string f = "'" /* comment8 */ + '"' /* comment9 */
            + '\"' + /* comment10*/ "\""; /* comment11*/

}