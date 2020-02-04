import matplotlib.pyplot as plt
import pandas as pd
import matplotlib as mpl
import os, sys
import ast
from matplotlib.pyplot import MultipleLocator


def draw_linechart(x_name, x_values, y_name, y_legends, y_values, save_file):
    mpl.rcParams['axes.linewidth'] = 1.2
    plt.rc('font', family='Helvetica', size=10, weight='roman')
    plt.rc('pdf', fonttype=42)
    # plt.figure(figsize=(4.0, 2.5))
    # plt.subplots_adjust(
    #     left=0.12,
    #     bottom=0.18,
    #     right=0.96,
    #     top=0.94,
    #     wspace=0.00,
    #     hspace=0.00)
    # plt.rcParams['xtick.direction'] = 'in'
    # plt.rcParams['ytick.direction'] = 'in'

    plt.xlabel(x_name)
    plt.ylabel(y_name)

    y_min = sys.maxint
    y_max = -sys.maxint
    for i in range(len(y_legends)):
        x_values[i] = [int(e) for e in x_values[i]]

        y_min = min(y_min, min(y_values[i]))
        y_max = max(y_max, max(y_values[i]))

        if y_max - y_min <= 70:
            plt.ylim(y_min - 10, y_min + 70)
        plt.plot(x_values[i], y_values[i])

    # y_major_locator = MultipleLocator(50)
    # plt.gca().yaxis.set_major_locator(y_major_locator)
    # marksize = 2
    # linewidth = 1

    plt.legend(labels=y_legends)
    # plt.show()
    plt.savefig(save_file)


def main():

    x_name = sys.argv[1]
    print x_name

    x_values = eval(sys.argv[2])
    print x_values

    y_legends = sys.argv[3].split(",")
    print y_legends

    y_name = sys.argv[4]
    print y_name

    y_values = eval(sys.argv[5])
    print y_values

    save_file = sys.argv[6]
    print save_file

    draw_linechart(x_name, x_values, y_name, y_legends, y_values, save_file)


if __name__ == '__main__':
    sys.exit(main())
