# comment 1

from itertools import permutations

def checkio(chips):
    return max(count(p, 0, None, None, 0) for p in permutations(chips))
​
def count(chips, i, first, last, sum):
    if i == 6:
        return sum if (first == last) else 0; # comment 2

    results = (-1,)
    for p in permutations(chips[i]):
        if (i == 0) or (last == p[0]):
            results += count(chips, i+1, first if i>0 else p[0], p[2], sum+p[1]),
    return max(results)

def comment_tests():
    s1 = "# not comment \" "  # comment 3
    s2 = """ this is a very   # this is not comment
        long string """       # comment 4

    s3 = '# not comment " \' '  # comment 5
    s4 = ''' this is a very     # this is not comment
        long string '''         # comment 6