# plot the data using ggplot2
library(ggplot2)
library(tidyverse)
library(plotly)

# Generates the graph where the difference in the number of nodes after reduction is shown
n_of_nodes_graph <- number_of_nodes_data %>%
  gather(key = "BDD_Type", value = "Number_of_Nodes", -Number_of_Variables) %>%
  ggplot(aes(x = Number_of_Variables, y = Number_of_Nodes, fill = BDD_Type)) +
  geom_area(alpha = 0.5, position = "identity") +
  scale_x_continuous(breaks = seq(min(number_of_nodes_data$Number_of_Variables), max(number_of_nodes_data$Number_of_Variables), 2)) +
  labs(title = "Comparison of BDD Types",
       x = "Number of Variables",
       y = "Number of Nodes After Reduction",
       fill = "BDD Type") -> ggplot_obj
ggplotly(ggplot_obj, tooltip = c("BDD_Type", "Number_of_Nodes"))

# Generates the graph where the difference in the reduction ratio is shown
r_r_graph <- reduction_ratio_data %>%
  gather(key = "BDD_Type", value = "Reduction_Ratio", -Number_of_Variables) %>%
  ggplot(aes(x = Number_of_Variables, y = Reduction_Ratio, fill = BDD_Type)) +
  geom_area(alpha = 0.5, position = "identity") +
  scale_x_continuous(breaks = seq(min(number_of_nodes_data$Number_of_Variables), max(number_of_nodes_data$Number_of_Variables), 2)) +
  labs(title = "Comparison of BDD Types",
       x = "Number of Variables",
       y = "Reduction Ratio (%)",
       fill = "BDD Type") -> ggplot_obj
ggplotly(ggplot_obj, tooltip = c("BDD_Type", "Reduction_Ratio"))

# Generates the graph where the difference in the reduction duration is shown
r_d_graph <- reduction_duration_data %>%
  gather(key = "BDD_Type", value = "Reduction_Duration", -Number_of_Variables) %>%
  ggplot(aes(x = Number_of_Variables, y = Reduction_Duration, fill = BDD_Type)) +
  geom_area(alpha = 0.5, position = "identity") +
  scale_x_continuous(breaks = seq(min(number_of_nodes_data$Number_of_Variables), max(number_of_nodes_data$Number_of_Variables), 2)) +
  labs(title = "Comparison of BDD Types",
       x = "Number of Variables",
       y = "Reduction (Creation) Duration (ns)",
       fill = "BDD Type") -> ggplot_obj
ggplotly(ggplot_obj, tooltip = c("BDD_Type", "Reduction_Duration"))

# Generates the graph. where the difference in the testing duration is shown
# (the time to test all combinations of inputs)
t_d_graph <- testing_duration_data %>%
  gather(key = "BDD_Type", value = "Testing_Duration", -Number_of_Variables) %>%
  ggplot(aes(x = Number_of_Variables, y = Testing_Duration, fill = BDD_Type)) +
  geom_area(alpha = 0.7, position = "identity") +
  scale_x_continuous(breaks = seq(min(number_of_nodes_data$Number_of_Variables), max(number_of_nodes_data$Number_of_Variables), 2)) +
  labs(title = "Comparison of BDD Types",
       x = "Number of Variables",
       y = "Testing Duration (ns)",
       fill = "BDD Type") -> ggplot_obj
ggplotly(ggplot_obj, tooltip = c("BDD_Type", "Testing_Duration"))