Program takes in directory via cli:

Example:
java -cp classes app.Main seuss

where it reads the seuss directory for texts

Internal function removes all existing punctuation including ' in words (ex. couldn't -> couldnt) in order for comparison purposes
Probabiliy is then calculated based on the frequency and count of next words in the directory folders