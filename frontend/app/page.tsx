'use client'

import { FoodItemType } from '@/app/types/food'
import React, { useEffect, useState } from 'react'
import {
    Card,
    Row,
    Col,
    PaginationProps,
    Flex,
    Pagination,
    Table,
    Button,
    Select,
    Skeleton,
} from 'antd'
import axios from 'axios'
import ProductSearch from '@/app/components/ProductSearch'
import FoodItem from '@/app/components/FoodItem'
import { InvertedIndexType } from '@/app/types/invertedindex'
import InvertedIndex from '@/app/components/InvertedIndex'
import { PageRankingDataType } from '@/app/types/pageranking'
import AppAutoComplete from './components/AppAutoComplete'
import { useAppStore } from '@/stores/app-store-provider'
import { useDebounce } from 'use-debounce'

export default function Products() {
    const [items, setItems] = useState<FoodItemType[]>([])
    const [loading, setLoading] = useState(false)

    const { searchValue, setSearchValue } = useAppStore((state) => state)
    const [category, setCategory] = useState('')
    const [store, setStore] = useState('')

    const [page, setPage] = useState(1)
    const [size, setSize] = useState(8)
    const [total, setTotal] = useState(0)

    const { spellCheckOptions, setSpellCheckOptions } = useAppStore(
        (state) => state
    )
    const { invertedIndexData, setInvertedIndexData } = useAppStore(
        (state) => state
    )
    const { pageRankingResult, setPageRankingResult } = useAppStore(
        (state) => state
    )
    const { topSearches, setTopSearches } = useAppStore((state) => state)
    const { recentSearches, setRecentSearches } = useAppStore((state) => state)

    const onFetchProducts = async () => {
        try {
            const response = await axios.get('/api/product', {
                params: {
                    q: searchValue,
                    page: page - 1,
                    size,
                    category,
                    store,
                },
            })
            setItems(response.data.content)
            setTotal(response.data.totalElements)
        } catch (error) {
            console.error('Fetch error:', error)
        } finally {
            setLoading(false)
        }
    }

    const [debouncedSearchValue] = useDebounce(searchValue, 200)
    const fetchSearchHistoryData = async () => {
        try {
            if (!!debouncedSearchValue) {
                const response = await axios.get('/api/keyword-search', {
                    params: { q: debouncedSearchValue },
                })
                const response2 = await axios.get('/api/keyword-search/list', {
                    params: { q: 'top' },
                })
                const response3 = await axios.get('/api/keyword-search/list', {
                    params: { q: 'recent' },
                })
                if (!!response.data) {
                    setTopSearches(response2.data)
                    setRecentSearches(response3.data)
                }
            }
        } catch (error) {
            console.error('Fetch error:', error)
        }
    }

    useEffect(() => {
        onFetchProducts()
    }, [size, page])

    const onSearch = async () => {
        setLoading(true)
        await Promise.all([
            onFetchProducts(),
            onSpellCheck(),
            onCheckInvertedIndex(),
            onFetchPageRankingData(),
            fetchSearchHistoryData(),
        ]).then(() => {
            setLoading(false)
        })
    }

    const onSpellCheck = async () => {
        try {
            const response = await axios.get('/api/spell-checking', {
                params: { word: searchValue },
            })
            setSpellCheckOptions(response.data)
        } catch (error) {
            console.error('Fetch error:', error)
        }
    }

    const onCheckInvertedIndex = async () => {
        try {
            const response = await axios.get('/api/inverted-index', {
                params: { query: searchValue },
            })
            setInvertedIndexData(response.data)
        } catch (error) {
            console.error('Fetch error:', error)
        }
    }

    const onFetchPageRankingData = async () => {
        try {
            setPageRankingResult([])
            const response = await axios.get('/api/page-ranking', {
                params: { search: searchValue },
            })

            setPageRankingResult(response.data)
        } catch (error) {
            console.error('Fetch error:', error)
        }
    }

    const onPaginationChange: PaginationProps['onShowSizeChange'] = (
        current,
        pageSize
    ) => {
        setPage(current)
        setSize(pageSize)
    }
    if (loading) {
        return <Skeleton loading style={{ height: '100vh', color: 'black' }} />
    }

    return (
        <div className='container mx-auto p-2'>
            <div className='grid grid-rows gap-2'>
                <Card title='Search'>
                    <div className='grid grid-cols-4 items-center gap-4'>
                        <AppAutoComplete
                            searchValue={searchValue}
                            setSearchValue={setSearchValue}
                            placeholder='Search food items'
                        />
                        <Select
                            value={category}
                            options={[
                                {
                                    value: '',
                                    label: 'All',
                                },
                                {
                                    value: 'Meat & Poultry',
                                    label: 'Meat & Poultry',
                                },
                                { value: 'Grocery', label: 'Grocery' },
                                {
                                    value: 'Fish & Seafood',
                                    label: 'Fish & Seafood',
                                },
                                { value: 'Bakery', label: 'Bakery' },
                                {
                                    value: 'Dairy & Eggs',
                                    label: 'Dairy & Eggs',
                                },
                                { value: 'Frozen Food', label: 'Frozen Food' },
                            ]}
                            onChange={(value) => setCategory(value)}
                            placeholder='Category'
                        />
                        <Select
                            value={store}
                            options={[
                                {
                                    value: '',
                                    label: 'All',
                                },
                                {
                                    value: 'multifood',
                                    label: 'Multifood',
                                },
                                { value: 'nofrills', label: 'NoFrills' },
                                {
                                    value: 'foodbasic',
                                    label: 'FoodBasic',
                                },
                                {
                                    value: 'loblaws',
                                    label: 'Loblaws',
                                },
                                {
                                    value: 'zehrs',
                                    label: 'Zehrs',
                                },
                            ]}
                            onChange={(value) => setStore(value)}
                            placeholder='Store'
                        />
                        <button
                            onClick={() => {
                                onSearch()
                            }}
                            className='px-4 py-2 bg-blue-500 text-white font-semibold rounded-lg shadow hover:bg-blue-600 transition duration-200'
                        >
                            Search
                        </button>
                    </div>
                </Card>

                <div className='grid grid-flow-col gap-2 grid-cols-7'>
                    <div className='grid grid-rows col-span-5 gap-2'>
                        <Card
                            title='Products'
                            actions={[
                                <Pagination
                                    className='inline-block'
                                    current={page}
                                    total={total}
                                    onChange={onPaginationChange}
                                    showSizeChanger
                                    pageSizeOptions={['8', '16', '24']}
                                    pageSize={size}
                                />,
                            ]}
                        >
                            <FoodItem items={items} />
                        </Card>

                        <InvertedIndex data={invertedIndexData} />
                    </div>
                    <div className='grid grid-rows col-span-2 gap-2'>
                        <Card title={'Do you mean'}>
                            {spellCheckOptions?.length > 0 ? (
                                <div className='grid grid-cols-2 gap-2'>
                                    {spellCheckOptions.map((option, i) => (
                                        <button
                                            key={i}
                                            onClick={() =>
                                                setSearchValue(option)
                                            }
                                            className='bg-gray-200 hover:bg-gray-300 text-gray-700 font-semibold py-2 px-4 rounded'
                                        >
                                            {option}
                                        </button>
                                    ))}
                                </div>
                            ) : (
                                <div>No suggestions</div>
                            )}
                        </Card>

                        <Card title='Search History'>
                            {topSearches?.length > 0 ? (
                                <Table
                                    columns={[
                                        {
                                            dataIndex: 'keyword',
                                            title: 'Keyword',
                                            ellipsis: true,
                                            width: 250,
                                        },
                                        {
                                            dataIndex: 'count',
                                            title: 'Count',
                                            width: 150,
                                        },
                                    ]}
                                    dataSource={topSearches}
                                />
                            ) : (
                                <div>No search history</div>
                            )}
                        </Card>

                        <Card
                            title='Ranking Page'
                            className='row-span-3 bg-white rounded-lg p-6 mb-4'
                        >
                            {pageRankingResult?.length > 0 ? (
                                <Table
                                    columns={[
                                        {
                                            dataIndex: 'url',
                                            title: 'URL',
                                            ellipsis: true,
                                            width: 250,
                                            render: (url: string) => (
                                                <a
                                                    href={url}
                                                    target='_blank'
                                                    title={url}
                                                >
                                                    {url}
                                                </a>
                                            ),
                                        },
                                        {
                                            dataIndex:
                                                'frequencyOfSearchKeyword',
                                            title: 'Frequency',
                                            width: 150,
                                        },
                                    ]}
                                    dataSource={pageRankingResult}
                                />
                            ) : (
                                <div>No ranking page</div>
                            )}
                        </Card>
                    </div>
                </div>
            </div>
        </div>
    )
}
