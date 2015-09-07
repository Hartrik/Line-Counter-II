/* comment1 */

var sum = function() {
    var i, x = 0;

    for (i = 0; i < arguments.length; ++i) { /* comment2 */
        x += /* comment3 */ arguments[i];
    }

    return x;
};

var number = 42; /*co
mm

ent*/

var s1 = "string /* this is not comment */";  // comment 5
var s2 = " // this is not comment ' \" ";     // comment 6
var s3 = 'string /* this is not comment */';  // comment 7
var s4 = ' // this is not comment " \' ';     // comment 8

var regex1 = /ab+c/;
var regex2 = /aa\/bb/; // comment 9
var regex3 = /aa"b'b/; // comment10