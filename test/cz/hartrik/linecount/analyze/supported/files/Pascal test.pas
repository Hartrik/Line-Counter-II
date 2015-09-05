Program HelloWorld(output);

// comment 1

Var
    Num1, Num2, Sum : Integer;

Begin
    Write('Input number 1:');  { comment2 }
    (* comment3 *)

    Readln(Num1);
    Writeln('Input number 2:');

    Readln(Num2); (*com
me
nt4*)

    Sum := (* comment5 *) Num1 + Num2; // comment 6
    Writeln(Sum);

    Writeln(' // text (* text { text ');  // comment 7
    Writeln(' val = ''text'' '); // comment 8

End.