#!/bin/bash
mysqldump -h$1 -P$2 -u$3 -p$4 $5 > $6
