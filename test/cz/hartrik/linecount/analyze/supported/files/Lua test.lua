-- comment 1

string_1 = [[
    string  --[ this is not comment
]]  -- comment 2

string_2 = [=[
    [[string]] \n   -- this is not comment
    [string]        -- this is not comment
]=]

print(string_1)
print(string_2) --[[co
mm

ent--]]

text_1 = " \" -- --[[ this is not comment  " -- comment 4
text_1 = ' \' -- --[[ this is not comment  ' -- comment 5

--[[ comment6 --]]