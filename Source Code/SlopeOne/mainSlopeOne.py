"""
@author:Zhao
@ide:PyCharm
@createTime:2018-11-21
"""
import time
import sys
import multiprocessing

import numpy as np

from publicTool.Tool import get_small_matrix, remove_entries, evaluate, Logger
from slopeOnePrediction import predict_for_all_user2

para = {
    'dataPath': '..\\DataSet\\rtData.txt',  # 文件路径
    'program path': 'D:\\PycharmProject\\smallPaper\\SlopeOneNew',
    'outPath': 'C:\\Users\\Administrator\\Desktop\\experiment data201903\\rt\\201903022SlopeOne339and5825_rt.txt',
    'parallelMode': True,  # 是否启用并行计算
    'threadNum': 20,
    'metrics': ['MAE', 'NMAE', 'RMSE'],
    'density': np.arange(0.05, 0.21, 0.05),  # 训练矩阵的密度
    'rounds': 20,  # 每个密度下重复计算的次数
    'numUser': 339,
    'numService': 5825,
    'method': 'slopeOne',
    'saveData': True  # 是否保存打印的数据
}


def executeOneSetting(data, density, numUser, numService, round_num):
    # np.set_printoptions(threshold=np.NaN, suppress=True, linewidth=100000)  # 打印显示设置
    # small_matrix = get_small_matrix(data, numUser, numService, seedId=round_num)
    removedMatrix, remaining_train_matrix = remove_entries(data, density, seedId=round_num)
    removedMatrixTranspose = np.transpose(removedMatrix).copy()
    iMean = np.sum(removedMatrixTranspose, axis=1) / (np.sum(removedMatrixTranspose > 0, axis=1) + np.spacing(1))
    predictMatrix = predict_for_all_user2(data, removedMatrix, removedMatrixTranspose, iMean, numUser,
                                          numService)
    mae, nmae, rmse = evaluate(predictMatrix, remaining_train_matrix)
    return mae, nmae, rmse


if __name__ == '__main__':
    startTime = time.clock()
    if para['saveData'] is True:
        sys.stdout = Logger(para['outPath'])  # 打印控制台数据
    print('Parameter setting:')
    print('-' * 130)
    for key, value in para.items():
        print('{key}:{value}'.format(key=key, value=value))
    print('-' * 130)
    data = np.loadtxt(para['dataPath'], dtype=float, delimiter="\t")
    print(data.shape)
    pool = multiprocessing.Pool(para['threadNum'])
    for density in para['density']:
        result_list = []
        list_mae = []
        list_nmae = []
        list_rmse = []
        print('=' * 130)
        print('density = %s' % density)
        for round_num in range(para['rounds']):
            result_list.append(
                pool.apply_async(executeOneSetting, (data, density, para['numUser'], para['numService'], round_num)))
        for i in range(len(result_list)):
            list_mae.append(result_list[i].get()[0])
            list_nmae.append(result_list[i].get()[1])
            list_rmse.append(result_list[i].get()[2])
        print('mae')
        print(list_mae)
        print(np.mean(list_mae))
        print('nmae')
        print(list_nmae)
        print(np.mean(list_nmae))
        print('rmse')
        print(list_rmse)
        print(np.mean(list_rmse))
    pool.close()
    pool.join()
    endTime = time.clock()
    print("程序运行时间：%d 秒" % (endTime - startTime))
