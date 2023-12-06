import os
from textblob import TextBlob
import csv
import dotenv

dotenv.load_dotenv()

def process_csv(input_file, positive_output_file, negative_output_file, neutral_output_file):
    with open(input_file, 'r', newline='', encoding='utf-8') as csv_input, \
         open(positive_output_file, 'w', newline='', encoding='utf-8') as positive_output, \
         open(negative_output_file, 'w', newline='', encoding='utf-8') as negative_output, \
         open(neutral_output_file, 'w', newline='', encoding='utf-8') as neutral_output:
         

        # CSV reader and writer objects
        reader = csv.reader(csv_input)
        # file_output = csv.writer(csv_output)

        # Write header to the output file (if there is one)
        header = next(reader, None)
        if header is not None:
            positive_output.write("".join(header) + "\n")
            negative_output.write("".join(header) + "\n")
            neutral_output.write("".join(header) + "\n")

        # Process each row in the input CSV
        negative_count = 0
        positive_count = 0
        neutral_count = 0
        for row in reader:
            # Check if the row has a label (0 or 1) at the beginning
            if row and row[0] in ('0', '1'): 
                # Write the row to the output CSV
                # writer.writerow(row)
                label = row[0] + "\t"
                data = ""
                for char in row[1]:
                    if char == "\n":
                        continue
                    if char.isalnum() or char.isspace():
                        data += char
                data += "\n"
                sentiment_analyzer = TextBlob(data)
                polarity = sentiment_analyzer.sentiment.polarity
                if polarity < -0.1 and negative_count <= 2000:
                    negative_count += 1
                    negative_output.write(label + data)  
                elif polarity > 0.1 and positive_count <= 2000:
                    positive_count += 1
                    positive_output.write(label + data)
                elif (polarity > -0.1 or polarity < 0.1) and neutral_count <= 2000:
                    neutral_count += 1
                    neutral_output.write(label + data)
                else:
                    break

# Example usage

if __name__ == "__main__":
    path = str(os.getenv('PATH_VARIABLE'))
    path1 = 'data/suicide-test-positive.txt'
    path2 = 'data/suicide-test-negative.txt'
    path3 = 'data/suicide-test-neutral.txt'
    process_csv(path, path1, path2, path3)
