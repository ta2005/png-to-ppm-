the optinal color will be then added

we have three diffrent parsing stratiegies(grammars)
the first one if the clor types 0 and 6

IHRD -> IDAT
IDAT -> IDAT|IEND
IEND -> end

if color type is 3

IHDR -> PLTE
PLTE -> IDAT
IDAT -> IDAT|IEND
IEND -> end

if color type is 2 or 6

IHDR -> PLTE|IDAT
PLTE -> IDAT
IDAT -> IDAT|IEND
IEND -> end
