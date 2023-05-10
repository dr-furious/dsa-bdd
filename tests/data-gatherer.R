rm(list = ls())
library(tidyverse)

get_data_from_file <- function (path, delimiter = ";") {
  my_data <- read_delim(path, delim = delimiter)
}

# specify the file names and paths
file_names <- character()
for (i in 1:23) {
  file_names <- c(file_names, paste0("test_", i, "01/results.csv"))
}
file_paths <- file.path("./", file_names)

# use map() function from purrr to read each file into a data frame and store in a list
my_data_list <- map(file_paths, get_data_from_file)

number_of_nodes_data <- 
  reduction_ratio_data <- 
  reduction_duration_data <- 
  testing_duration_data <- 
  tibble(
    Number_of_Variables = numeric(),
    Alphabetically_Ordered_BDD = numeric(), 
    Best_Ordered_BDD = numeric())

for(i in 1:23) {
  curr_sample = my_data_list[[i]]
  number_of_nodes_data <- number_of_nodes_data %>% add_row(
    Number_of_Variables = i,
    Alphabetically_Ordered_BDD = curr_sample$Nodes_After_Reduction[1], 
    Best_Ordered_BDD = curr_sample$Nodes_After_Reduction[2]
    )
  
  reduction_ratio_data <- reduction_ratio_data %>% add_row(
    Number_of_Variables = i,
    Alphabetically_Ordered_BDD = curr_sample$Reduction_Ratio[1], 
    Best_Ordered_BDD = curr_sample$Reduction_Ratio[2]
  )
  
  reduction_duration_data <- reduction_duration_data %>% add_row(
    Number_of_Variables = i,
    Alphabetically_Ordered_BDD = curr_sample$Reduction_Duration[1], 
    Best_Ordered_BDD = curr_sample$Reduction_Duration[2]
  )
  
  testing_duration_data <- testing_duration_data %>% add_row(
    Number_of_Variables = i,
    Alphabetically_Ordered_BDD = curr_sample$Testing_Duration[1], 
    Best_Ordered_BDD = curr_sample$Testing_Duration[2]
  )
}

