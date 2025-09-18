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

import { Container, Paper, Box, Typography, Alert } from "@mui/material";
import { Bar } from "react-chartjs-2";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

// USE EFFECT TO FETCH PORTFOLIO FROM URL
//REFERENCE CONTEXT TO GET USER OBJ OR ID

function OrderHistory() {
  const [portfolio, setPortfolio] = useState(); // one portfolio
  const [userOrders, setOrders] = useState([]); // used for graph
  const [userStocks, setStocks] = useState([]); // used for graph
  const [displayInfo, setDisplayInfo] = useState([]); // consolidates orders and stock info to display
  const [error, setError] = useState("");

  const { id } = useParams();

  const token =
    localStorage.getItem("token") || sessionStorage.getItem("token");
  const url = `http://localhost:8080/api/portfolio/port`;

  useEffect(() => {
    fetch(`${url}/${id}`, {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        } else {
          return Promise.reject(`Unexpected Status Code: ${response.status}`);
        }
      })
      .then((data) => {
        setPortfolio(data);
        sortStocksOrders(data);
        processDisplayInfo(data);
      }).catch((error) => {
        setError(error.message || "Portfolio failed to load!");
      });
  }, [token, url, id]);

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
    labels: userStocks.map((stock) => stock.name),
    datasets: [
      {
        label: "Cost",
        data: userOrders.map((order) => order.price),
        backgroundColor: ["rgba(255, 99, 132, 0.2)"],
        borderColor: ["rgba(255, 99, 132, 0.2)"],
        borderWidth: 1,
      },
    ],
  };

  function sortStocksOrders(data) {
    setOrders(data.orders.sort((a, b) => a.stockId - b.stockId));
    setStocks(data.stocks.sort((a, b) => a.id - b.id));
    return;
  }

  function processDisplayInfo(data) {
    let orderedOrders = data.orders.sort((a, b) => a.stockId - b.stockId);
    let orderedStocks = data.stocks.sort((a, b) => a.id - b.id);

    let totalInfo = []
    for(let i = 0; i < orderedOrders.length; i++){
      let currentOrder = orderedOrders.at(i);
      let currentStock = orderedStocks.at(i);
      totalInfo.push(
        {
          orderTransactionType : currentOrder.transactionType,
          orderSharesAmount : currentOrder.numberOfShares,
          orderPrice : currentOrder.price,
          orderDate : currentOrder.date,
          stockName : currentStock.name,
          stockPrice : currentStock.currentPrice
        }
      )
    }

    setDisplayInfo(totalInfo);

    return;
  }

  return (
    <>
      {portfolio ? (
        <>
          <Container maxWidth="lg">
            <Paper elevation={4} sx={{ mt: 4, p: 4, borderRadius: 10 }}>
              {error && (
                <Alert severity="error" sx={{ mb: 4 }}>
                  {error}
                </Alert>
              )}
              {displayInfo.map((info) => (
                <Box sx={{ display: "flex", gap: 3, overflowX: "auto", pb: 2, mt: 3 }}>
                  <Typography variant="h4" align="center">
                    {info.stockName}
                  </Typography>
                  <Typography variant="body1">Date: {info.orderDate}</Typography>
                  <Typography variant="body1">
                    Transaction Type: {info.orderTransactionType}
                  </Typography>
                  <Typography variant="body1">
                    Shares: {info.orderSharesAmount}
                  </Typography>
                  <Typography variant="body1">
                    Total Price: ${info.orderPrice}
                  </Typography>
                  <Typography variant="body1">
                    Stock Price: ${info.stockPrice}
                  </Typography>
                </Box>
              ))}
              <Bar options={options} data={orderHistory} />
            </Paper>
          </Container>
        </>
      ) : (
        <div> Loading... </div>
      )}
    </>
  );
}

export default OrderHistory;
