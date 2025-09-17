import React, { use } from "react";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  scales,
} from "chart.js";
import { Container, Paper, Box, Typography } from "@mui/material";
import { Bar } from "react-chartjs-2";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

// USE EFFECT TO FETCH PORTFOLIO FROM URL
//REFERENCE CONTEXT TO GET USER OBJ OR ID

function OrderHistory() {
  const [portfolio, setPortfolio] = useState(); // one portfolio
  const [userOrders, setOrders] = useState([]);
  const [userStocks, setStocks] = useState([]);

  const { id } = useParams();

  const token = localStorage.getItem('token');
  const url = `http://localhost:8080/api/portfolio/${id}`;

  useEffect(() => {
    fetch(url, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    })
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        } else {
          return Promise.reject(`Unexpected Status Code: ${response.status}`);
        }
      })
      .then((data) => setPortfolio(data))
      .catch(console.log);
  }, [url]);

  /*
  const userOrders = portfolio.orders;
  userOrders.sort();
  const userStocks = portfolio.stocks;
  userStocks.sort();
  */

  setOrders(portfolio.orders.sort((a, b) => a.stockId - b.stockId));
  setStocks(portfolio.stocks.sort((a, b) => a.id - b.id));

  ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
  );

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: "top",
      },
      title: {
        display: true,
        text: `Your Order History`,
      },
    },
  };

  const orderHistory = {
    labels: userStocks.map((stock) => stock.name), // check if order and stock are same before
    datasets: [
      {
        label: "Cost",
        data: userOrders.map((order) => order.price), //need to dynamically introduce data
        backgroundColor: ["rgba(255, 99, 132, 0.2)"],
        borderColor: ["rgba(255, 99, 132, 0.2)"],
        borderWidth: 1,
      },
    ],
  };

  function getStockName(orderStockId) {
    userStocks.forEach((stock) => {
      if (stock.id === orderStockId) {
        return stock.name;
      }
    });
  }

  function getStockPrice(orderStockId) {
    userStocks.forEach((stock) => {
      if (stock.id === orderStockId) {
        return stock.currentPrice;
      }
    });
  }

  // format page in to display stock and order info
  return (
    <>
      <Container maxWidth="lg">
        <Paper elevation={4} sx={{ mt: 4, p: 4, borderRadius: 10 }}>
          {userOrders.map((order) => (
            <Box>
              <Typography variant="h2" align="center">
                ${getStockName(order.stockId)}
              </Typography>
              <Typography variant="body1">Date: ${order.date}</Typography>
              <Typography variant="body1">
                Transaction Type: ${order.transactionType}
              </Typography>
              <Typography variant="body1">Shares: ${order.shares}</Typography>
              <Typography variant="body1">
                Total Price: ${order.price}
              </Typography>
              <Typography variant="body1">
                Stock Price: ${getStockPrice(order.stockId)}
              </Typography>
            </Box>
          ))}
          <Bar options={options} data={orderHistory} />
        </Paper>
      </Container>
    </>
  );
}

export default OrderHistory;
