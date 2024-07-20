'use client'

import { Input, Table, Typography } from 'antd'
import axios from 'axios'
import React, { useEffect, useState } from 'react'
import { InvertedIndexType } from '../types/invertedindex'

function InvertedIndex() {
    const [searchValue, setSearchValue] = useState<string>('')
    const [data, setData] = useState<InvertedIndexType[]>([])

    useEffect(() => {
        const fetchInvetedIndex = async () => {
            try {
                const response = await axios.get('/api/inverted-index', {
                    params: {
                        query: searchValue,
                    },
                })
                setData(response.data)
            } catch (error) {
                console.error(error)
            }
        }
        fetchInvetedIndex()
    }, [searchValue])

    const options = [
        {
            dataIndex: 'docId',
            title: 'Document ID',
        },
        {
            dataIndex: 'name',
            title: 'Name',
        },
        {
            dataIndex: 'description',
            title: 'Description',
        },
    ]

    return (
        <div>
            <Typography.Title>Inverted Indexing</Typography.Title>
            <Input
                value={searchValue}
                onChange={(e) => setSearchValue(e.target.value)}
            />
            <Table dataSource={data} columns={options}></Table>
        </div>
    )
}

export default InvertedIndex
