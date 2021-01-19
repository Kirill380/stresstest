# Stress test results

Parameters: stress testing during 5 minutes, 100 unique users

## Without cache

| Type/Concurrency    | 10          | 25          | 50          | 100         |
|---------------------|-------------|-------------|-------------|-------------|
| Transaction rate    | 306.81      | 325.75      | 328.07      | 309.56      |
| Response time       | 30 ms       | 80 ms       | 150 ms      | 320 ms      |
| Success rate        | 91783/91783 | 97502/97502 | 98268/98268 | 92804/92804 |
| Longest transaction | 950 ms      | 1490 ms     | 1410 ms     | 2770 ms     |

## With cache 

| Type/Concurrency    | 10            | 25            | 50            | 100           |
|---------------------|---------------|---------------|---------------|---------------|
| Transaction rate    | 384.44        | 375.26        | 416.43        | 415.30        |
| Response time       | 30 ms         | 70 ms         | 120 ms        | 240 ms        |
| Success rate        | 115235/115235 | 112251/112251 | 124868/124868 | 124536/124536 |
| Longest transaction | 6940 ms       | 8400 ms       | 6550 ms       | 10600 ms      |

## With Cache and probabilistic cache flushing

| Type/Concurrency    | 10            | 25            | 50            | 100           |
|---------------------|---------------|---------------|---------------|---------------|
| Transaction rate    | 435.08        | 442.62        | 448.18        | 453.93        |
| Response time       | 20 ms         | 50 ms         | 90 ms         | 220 ms        |
| Success rate        | 130372/130372 | 132754/132754 | 134236/134236 | 136139/136139 |
| Longest transaction | 13390 ms      | 13270 ms      | 20300 ms      | 21470 ms      |
