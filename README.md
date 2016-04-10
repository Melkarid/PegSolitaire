# PegSolitaire
Algorithm designed to solve a given HiRiQ (modified Peg Solitaire) configuration
INPUT: Boolean array of length 33
OUTPUT: Printing: original state, String to reach solved state (if possible), number of configs checked, solved state

example of INPUT:

boolean [] config = {false, true, false, false, true, false, true, false, false, false, false, false, false, false, true, false, true, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false}

solve(config);

OUTPUT: 

                  [@][ ][@]
                  [@][ ][@]
            [ ][@][@][@][@][@][@]
            [@][ ][@][ ][@][ ][ ]
            [@][@][@][@][@][@][@]
                  [@][@][@]
                  [@][@][@]
            Moves to solve configuration: 1@9, 4@16, 6@8, 7@9, 4@16, 17@19, 15@17, 14@16
            Number of configurations checked: 1137
                  [@][@][@]
                  [@][@][@]
            [@][@][@][@][@][@][@]
            [@][@][@][ ][@][@][@]
            [@][@][@][@][@][@][@]
                  [@][@][@]
                  [@][@][@]
